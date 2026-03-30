package com.association.gestiondons.service.impl;

import com.association.gestiondons.dto.request.CampagneRequest;
import com.association.gestiondons.dto.response.CampagneResponse;
import com.association.gestiondons.entity.Campagne;
import com.association.gestiondons.entity.StatutCampagne;
import com.association.gestiondons.exception.BusinessException;
import com.association.gestiondons.exception.ResourceNotFoundException;
import com.association.gestiondons.mapper.CampagneMapper;
import com.association.gestiondons.repository.CampagneRepository;
import com.association.gestiondons.service.CampagneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CampagneServiceImpl implements CampagneService {

    private final CampagneRepository campagneRepository;
    private final CampagneMapper     campagneMapper;

    @Override
    public List<CampagneResponse> findAll() {
        log.debug("Récupération de toutes les campagnes");
        return campagneRepository.findAll()
                .stream()
                .map(this::enrichResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CampagneResponse findById(Long id) {
        log.debug("Recherche campagne id={}", id);
        Campagne campagne = campagneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campagne", id));
        return enrichResponse(campagne);
    }

    @Override
    public List<CampagneResponse> findByStatut(StatutCampagne statut) {
        return campagneRepository.findByStatut(statut)
                .stream()
                .map(this::enrichResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampagneResponse> searchByTitre(String titre) {
        return campagneRepository.findByTitreContainingIgnoreCase(titre)
                .stream()
                .map(this::enrichResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CampagneResponse create(CampagneRequest request) {
        log.info("Création d'une campagne : {}", request.getTitre());
        validateDates(request);
        Campagne campagne = campagneMapper.toEntity(request);
        campagne = campagneRepository.save(campagne);
        return enrichResponse(campagne);
    }

    @Override
    @Transactional
    public CampagneResponse update(Long id, CampagneRequest request) {
        log.info("Mise à jour campagne id={}", id);
        Campagne campagne = campagneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campagne", id));
        validateDates(request);
        campagneMapper.updateFromRequest(request, campagne);
        campagne = campagneRepository.save(campagne);
        return enrichResponse(campagne);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Suppression campagne id={}", id);
        if (!campagneRepository.existsById(id)) {
            throw new ResourceNotFoundException("Campagne", id);
        }
        long nbDons = campagneRepository.countDonsByCampagneId(id);
        if (nbDons > 0) {
            throw new BusinessException(
                "Impossible de supprimer cette campagne : elle contient " + nbDons + " don(s)."
            );
        }
        campagneRepository.deleteById(id);
    }

    // ── Méthodes privées ────────────────────────────────────────

    private CampagneResponse enrichResponse(Campagne campagne) {
        CampagneResponse response = campagneMapper.toResponse(campagne);

        BigDecimal collecte = campagneRepository.sumMontantByCampagneId(campagne.getId());
        long nbDons         = campagneRepository.countDonsByCampagneId(campagne.getId());

        response.setMontantCollecte(collecte);
        response.setNombreDons(nbDons);

        if (campagne.getObjectif() != null && campagne.getObjectif().compareTo(BigDecimal.ZERO) > 0) {
            double pct = collecte.divide(campagne.getObjectif(), 4, RoundingMode.HALF_UP)
                                 .multiply(BigDecimal.valueOf(100))
                                 .doubleValue();
            response.setProgression(Math.min(pct, 100.0));
        }
        return response;
    }

    private void validateDates(CampagneRequest request) {
        if (request.getFin() != null && request.getFin().isBefore(request.getDebut())) {
            throw new BusinessException("La date de fin doit être postérieure à la date de début.");
        }
    }
}
