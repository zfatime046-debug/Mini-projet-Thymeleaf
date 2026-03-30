package com.association.gestiondons.service;

import com.association.gestiondons.dto.request.DonateurRequest;
import com.association.gestiondons.dto.response.DonateurResponse;
import com.association.gestiondons.entity.TypeDonateur;

import java.util.List;

public interface DonateurService {

    List<DonateurResponse> findAll();

    DonateurResponse findById(Long id);

    List<DonateurResponse> findByType(TypeDonateur type);

    List<DonateurResponse> searchByNom(String nom);

    DonateurResponse create(DonateurRequest request);

    DonateurResponse update(Long id, DonateurRequest request);

    void delete(Long id);
}
