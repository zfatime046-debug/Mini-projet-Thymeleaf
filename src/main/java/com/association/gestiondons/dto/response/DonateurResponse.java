package com.association.gestiondons.dto.response;

import com.association.gestiondons.entity.TypeDonateur;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DonateurResponse {
    private Long id;
    private String nom;
    private String email;
    private TypeDonateur type;

    // Champs calculés
    private BigDecimal totalDonne;
    private long nombreDons;
}
