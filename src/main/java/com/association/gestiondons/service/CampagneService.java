package com.association.gestiondons.service;

import com.association.gestiondons.dto.request.CampagneRequest;
import com.association.gestiondons.dto.response.CampagneResponse;
import com.association.gestiondons.entity.StatutCampagne;

import java.util.List;

public interface CampagneService {

    List<CampagneResponse> findAll();

    CampagneResponse findById(Long id);

    List<CampagneResponse> findByStatut(StatutCampagne statut);

    List<CampagneResponse> searchByTitre(String titre);

    CampagneResponse create(CampagneRequest request);

    CampagneResponse update(Long id, CampagneRequest request);

    void delete(Long id);
}
