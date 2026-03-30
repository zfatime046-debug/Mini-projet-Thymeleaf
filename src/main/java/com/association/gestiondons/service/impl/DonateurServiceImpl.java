package com.association.gestiondons.service.impl;

import com.association.gestiondons.dto.request.DonateurRequest;
import com.association.gestiondons.dto.response.DonateurResponse;
import com.association.gestiondons.entity.Donateur;
import com.association.gestiondons.entity.TypeDonateur;
import com.association.gestiondons.exception.BusinessException;
import com.association.gestiondons.exception.ResourceNotFoundException;
import com.association.gestiondons.mapper.DonateurMapper;
import com.association.gestiondons.repository.DonateurRepository;
import com.association.gestiondons.service.DonateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DonateurServiceImpl implements DonateurService {

    private final DonateurRepository donateurRepository;
    private final DonateurMapper     donateurMapper;

    @Override
    public List<DonateurResponse> findAll() {
        log.debug("Récupération de tous les donateurs");
        return donateurRepository.findAll()
                .stream()
                .map(this::enrichResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DonateurResponse findById(Long id) {
        log.debug("Recherche donateur id={}", id);
        Donateur donateur = donateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donateur", id));
        return enrichResponse(donateur);
    }

    @Override
    public List<DonateurResponse> findByType(TypeDonateur type) {
        return donateurRepository.findByTypeOrderByNom(type)
                .stream()
                .map(this::enrichResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonateurResponse> searchByNom(String nom) {
        return donateurRepository.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(this::enrichResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DonateurResponse create(DonateurRequest request) {
        log.info("Création donateur : {}", request.getEmail());
        if (donateurRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Un donateur avec l'email '" + request.getEmail() + "' existe déjà.");
        }
        Donateur donateur = donateurMapper.toEntity(request);
        donateur = donateurRepository.save(donateur);
        return enrichResponse(donateur);
    }

    @Override
    @Transactional
    public DonateurResponse update(Long id, DonateurRequest request) {
        log.info("Mise à jour donateur id={}", id);
        Donateur donateur = donateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donateur", id));

        // Vérifier unicité email si changé
        if (!donateur.getEmail().equalsIgnoreCase(request.getEmail())
                && donateurRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("L'email '" + request.getEmail() + "' est déjà utilisé.");
        }

        donateurMapper.updateFromRequest(request, donateur);
        donateur = donateurRepository.save(donateur);
        return enrichResponse(donateur);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Suppression donateur id={}", id);
        Donateur donateur = donateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donateur", id));
        if (!donateur.getDons().isEmpty()) {
            throw new BusinessException(
                "Impossible de supprimer ce donateur : il a " + donateur.getDons().size() + " don(s) enregistré(s)."
            );
        }
        donateurRepository.deleteById(id);
    }

    // ── Méthodes privées ────────────────────────────────────────

    private DonateurResponse enrichResponse(Donateur donateur) {
        DonateurResponse response = donateurMapper.toResponse(donateur);
        BigDecimal total = donateurRepository.sumMontantByDonateurId(donateur.getId());
        long nbDons      = donateur.getDons().size();
        response.setTotalDonne(total);
        response.setNombreDons(nbDons);
        return response;
    }
}
