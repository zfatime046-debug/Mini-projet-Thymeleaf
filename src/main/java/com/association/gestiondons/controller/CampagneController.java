package com.association.gestiondons.controller;

import com.association.gestiondons.dto.request.CampagneRequest;
import com.association.gestiondons.dto.response.ApiResponse;
import com.association.gestiondons.dto.response.CampagneResponse;
import com.association.gestiondons.entity.StatutCampagne;
import com.association.gestiondons.service.CampagneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campagnes")
@RequiredArgsConstructor
public class CampagneController {

    private final CampagneService campagneService;

    /**
     * GET /api/campagnes
     * GET /api/campagnes?statut=ACTIVE
     * GET /api/campagnes?titre=eau
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CampagneResponse>>> getAll(
            @RequestParam(required = false) StatutCampagne statut,
            @RequestParam(required = false) String titre) {

        List<CampagneResponse> result;
        if (statut != null) {
            result = campagneService.findByStatut(statut);
        } else if (titre != null && !titre.isBlank()) {
            result = campagneService.searchByTitre(titre);
        } else {
            result = campagneService.findAll();
        }
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /** GET /api/campagnes/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CampagneResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(campagneService.findById(id)));
    }

    /** POST /api/campagnes */
    @PostMapping
    public ResponseEntity<ApiResponse<CampagneResponse>> create(
            @Valid @RequestBody CampagneRequest request) {
        CampagneResponse created = campagneService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    /** PUT /api/campagnes/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CampagneResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CampagneRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Campagne mise à jour", campagneService.update(id, request)));
    }

    /** DELETE /api/campagnes/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        campagneService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
