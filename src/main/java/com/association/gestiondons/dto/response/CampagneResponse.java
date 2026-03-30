package com.association.gestiondons.dto.response;

import com.association.gestiondons.entity.StatutCampagne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CampagneResponse {
    private Long id;
    private String titre;
    private BigDecimal objectif;
    private LocalDate debut;
    private LocalDate fin;
    private StatutCampagne statut;

    // Champs calculés
    private BigDecimal montantCollecte;
    private long nombreDons;
    private double progression; // % atteint
}
