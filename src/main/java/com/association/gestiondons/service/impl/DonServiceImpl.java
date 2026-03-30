package com.association.gestiondons.service.impl;

import com.association.gestiondons.dto.request.DonFilterRequest;
import com.association.gestiondons.dto.request.DonRequest;
import com.association.gestiondons.dto.response.DonResponse;
import com.association.gestiondons.dto.response.StatistiquesResponse;
import com.association.gestiondons.entity.Campagne;
import com.association.gestiondons.entity.Don;
import com.association.gestiondons.entity.Donateur;
import com.association.gestiondons.entity.TypeDonateur;
import com.association.gestiondons.exception.ResourceNotFoundException;
import com.association.gestiondons.mapper.DonMapper;
import com.association.gestiondons.repository.CampagneRepository;
import com.association.gestiondons.repository.DonateurRepository;
import com.association.gestiondons.repository.DonRepository;
import com.association.gestiondons.service.DonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DonServiceImpl implements DonService {

    private final DonRepository       donRepository;
    private final CampagneRepository  campagneRepository;
    private final DonateurRepository  donateurRepository;
    private final DonMapper           donMapper;

    @Override
    public List<DonResponse> findAll() {
        log.debug("Récupération de tous les dons");
        return donRepository.findAll()
                .stream()
                .map(donMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DonResponse findById(Long id) {
        Don don = donRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Don", id));
        return donMapper.toResponse(don);
    }

    @Override
    public List<DonResponse> findWithFilters(DonFilterRequest filter) {
        log.debug("Filtrage des dons : {}", filter);
        return donRepository.findWithFilters(
                filter.getCampagneId(),
                filter.getMoyen(),
                filter.getDebut(),
                filter.getFin()
        ).stream().map(donMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<DonResponse> findByCampagne(Long campagneId) {
        if (!campagneRepository.existsById(campagneId)) {
            throw new ResourceNotFoundException("Campagne", campagneId);
        }
        return donRepository.findByCampagneId(campagneId)
                .stream().map(donMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<DonResponse> findByDonateur(Long donateurId) {
        if (!donateurRepository.existsById(donateurId)) {
            throw new ResourceNotFoundException("Donateur", donateurId);
        }
        return donRepository.findByDonateurId(donateurId)
                .stream().map(donMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DonResponse create(DonRequest request) {
        log.info("Enregistrement d'un don : montant={}, campagne={}, donateur={}",
                request.getMontant(), request.getCampagneId(), request.getDonateurId());

        Campagne campagne = campagneRepository.findById(request.getCampagneId())
                .orElseThrow(() -> new ResourceNotFoundException("Campagne", request.getCampagneId()));

        Donateur donateur = donateurRepository.findById(request.getDonateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Donateur", request.getDonateurId()));

        Don don = donMapper.toEntity(request);
        don.setCampagne(campagne);
        don.setDonateur(donateur);
        don = donRepository.save(don);

        return donMapper.toResponse(don);
    }

    @Override
    @Transactional
    public DonResponse update(Long id, DonRequest request) {
        log.info("Mise à jour don id={}", id);
        Don don = donRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Don", id));

        Campagne campagne = campagneRepository.findById(request.getCampagneId())
                .orElseThrow(() -> new ResourceNotFoundException("Campagne", request.getCampagneId()));

        Donateur donateur = donateurRepository.findById(request.getDonateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Donateur", request.getDonateurId()));

        donMapper.updateFromRequest(request, don);
        don.setCampagne(campagne);
        don.setDonateur(donateur);
        don = donRepository.save(don);

        return donMapper.toResponse(don);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Suppression don id={}", id);
        if (!donRepository.existsById(id)) {
            throw new ResourceNotFoundException("Don", id);
        }
        donRepository.deleteById(id);
    }

    // ── Statistiques ────────────────────────────────────────────

    @Override
    public StatistiquesResponse getStatistiques() {
        log.debug("Calcul des statistiques globales");

        BigDecimal total   = donRepository.totalMontant();
        BigDecimal panier  = donRepository.panierMoyen();
        long nbDons        = donRepository.count();
        long nbDonateurs   = donateurRepository.count();
        long nbCampActives = campagneRepository.findByStatut(
                com.association.gestiondons.entity.StatutCampagne.ACTIVE).size();

        // Stats par campagne
        List<StatistiquesResponse.StatCampagne> parCampagne = donRepository.statsParCampagne()
                .stream()
                .map(row -> {
                    String titre      = (String) row[0];
                    BigDecimal montant = (BigDecimal) row[1];
                    long nb           = ((Number) row[2]).longValue();
                    // Récupérer l'objectif
                    BigDecimal objectif = campagneRepository.findAll().stream()
                            .filter(c -> c.getTitre().equals(titre))
                            .map(Campagne::getObjectif)
                            .findFirst().orElse(BigDecimal.ONE);
                    double pct = montant.divide(objectif, 4, RoundingMode.HALF_UP)
                                       .multiply(BigDecimal.valueOf(100)).doubleValue();
                    return StatistiquesResponse.StatCampagne.builder()
                            .titre(titre)
                            .montantCollecte(montant)
                            .objectif(objectif)
                            .progression(Math.min(pct, 100.0))
                            .nombreDons(nb)
                            .build();
                }).collect(Collectors.toList());

        // Stats par moyen de paiement
        Map<String, BigDecimal> parMoyen = new LinkedHashMap<>();
        donRepository.statsParMoyen().forEach(row ->
                parMoyen.put(row[0].toString(), (BigDecimal) row[1]));

        // Donateurs par type
        Map<String, Long> donateurParType = new LinkedHashMap<>();
        for (TypeDonateur type : TypeDonateur.values()) {
            long count = donateurRepository.findByType(type).size();
            if (count > 0) donateurParType.put(type.name(), count);
        }

        return StatistiquesResponse.builder()
                .totalCollecte(total)
                .panierMoyen(panier)
                .nombreDons(nbDons)
                .nombreDonateurs(nbDonateurs)
                .nombreCampagnesActives(nbCampActives)
                .parCampagne(parCampagne)
                .parMoyen(parMoyen)
                .donateurParType(donateurParType)
                .build();
    }
}
