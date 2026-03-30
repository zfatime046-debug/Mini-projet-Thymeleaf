package com.association.gestiondons.controller;

import com.association.gestiondons.dto.request.DonateurRequest;
import com.association.gestiondons.dto.response.ApiResponse;
import com.association.gestiondons.dto.response.DonateurResponse;
import com.association.gestiondons.entity.TypeDonateur;
import com.association.gestiondons.service.DonateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donateurs")
@RequiredArgsConstructor
public class DonateurController {

    private final DonateurService donateurService;

    /**
     * GET /api/donateurs
     * GET /api/donateurs?type=ENTREPRISE
     * GET /api/donateurs?nom=ahmed
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DonateurResponse>>> getAll(
            @RequestParam(required = false) TypeDonateur type,
            @RequestParam(required = false) String nom) {

        List<DonateurResponse> result;
        if (type != null) {
            result = donateurService.findByType(type);
        } else if (nom != null && !nom.isBlank()) {
            result = donateurService.searchByNom(nom);
        } else {
            result = donateurService.findAll();
        }
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /** GET /api/donateurs/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DonateurResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(donateurService.findById(id)));
    }

    /** POST /api/donateurs */
    @PostMapping
    public ResponseEntity<ApiResponse<DonateurResponse>> create(
            @Valid @RequestBody DonateurRequest request) {
        DonateurResponse created = donateurService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    /** PUT /api/donateurs/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DonateurResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody DonateurRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Donateur mis à jour", donateurService.update(id, request)));
    }

    /** DELETE /api/donateurs/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        donateurService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
