package com.association.gestiondons.controller;

import com.association.gestiondons.dto.request.DonFilterRequest;
import com.association.gestiondons.dto.request.DonRequest;
import com.association.gestiondons.dto.response.ApiResponse;
import com.association.gestiondons.dto.response.DonResponse;
import com.association.gestiondons.dto.response.StatistiquesResponse;
import com.association.gestiondons.entity.MoyenPaiement;
import com.association.gestiondons.service.DonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dons")
@RequiredArgsConstructor
public class DonController {

    private final DonService donService;

    /**
     * GET /api/dons
     * GET /api/dons?campagneId=1&moyen=VIREMENT&debut=2025-01-01&fin=2025-12-31
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DonResponse>>> getAll(
            @RequestParam(required = false) Long campagneId,
            @RequestParam(required = false) MoyenPaiement moyen,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        DonFilterRequest filter = new DonFilterRequest();
        filter.setCampagneId(campagneId);
        filter.setMoyen(moyen);
        filter.setDebut(debut);
        filter.setFin(fin);

        boolean hasFilter = campagneId != null || moyen != null || debut != null || fin != null;
        List<DonResponse> result = hasFilter
                ? donService.findWithFilters(filter)
                : donService.findAll();

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /** GET /api/dons/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DonResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(donService.findById(id)));
    }

    /** GET /api/dons/campagne/{campagneId} */
    @GetMapping("/campagne/{campagneId}")
    public ResponseEntity<ApiResponse<List<DonResponse>>> getByCampagne(@PathVariable Long campagneId) {
        return ResponseEntity.ok(ApiResponse.ok(donService.findByCampagne(campagneId)));
    }

    /** GET /api/dons/donateur/{donateurId} */
    @GetMapping("/donateur/{donateurId}")
    public ResponseEntity<ApiResponse<List<DonResponse>>> getByDonateur(@PathVariable Long donateurId) {
        return ResponseEntity.ok(ApiResponse.ok(donService.findByDonateur(donateurId)));
    }

    /** GET /api/dons/statistiques */
    @GetMapping("/statistiques")
    public ResponseEntity<ApiResponse<StatistiquesResponse>> getStatistiques() {
        return ResponseEntity.ok(ApiResponse.ok(donService.getStatistiques()));
    }

    /** POST /api/dons */
    @PostMapping
    public ResponseEntity<ApiResponse<DonResponse>> create(
            @Valid @RequestBody DonRequest request) {
        DonResponse created = donService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    /** PUT /api/dons/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DonResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody DonRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Don mis à jour", donService.update(id, request)));
    }

    /** DELETE /api/dons/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        donService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
