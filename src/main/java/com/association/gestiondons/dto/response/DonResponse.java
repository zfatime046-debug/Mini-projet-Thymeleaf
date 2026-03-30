package com.association.gestiondons.dto.response;

import com.association.gestiondons.entity.MoyenPaiement;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DonResponse {
    private Long id;
    private BigDecimal montant;
    private LocalDate dateDon;
    private MoyenPaiement moyen;

    // Références imbriquées (pas les entités complètes)
    private Long campagneId;
    private String campagneTitre;
    private Long donateurId;
    private String donateurNom;
    private String donateurEmail;
}
