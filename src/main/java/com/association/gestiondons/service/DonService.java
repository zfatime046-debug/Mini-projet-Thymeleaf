package com.association.gestiondons.service;

import com.association.gestiondons.dto.request.DonFilterRequest;
import com.association.gestiondons.dto.request.DonRequest;
import com.association.gestiondons.dto.response.DonResponse;
import com.association.gestiondons.dto.response.StatistiquesResponse;

import java.util.List;

public interface DonService {

    List<DonResponse> findAll();

    DonResponse findById(Long id);

    List<DonResponse> findWithFilters(DonFilterRequest filter);

    List<DonResponse> findByCampagne(Long campagneId);

    List<DonResponse> findByDonateur(Long donateurId);

    DonResponse create(DonRequest request);

    DonResponse update(Long id, DonRequest request);

    void delete(Long id);

    StatistiquesResponse getStatistiques();
}
