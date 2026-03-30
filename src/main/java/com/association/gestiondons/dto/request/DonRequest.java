package com.association.gestiondons.dto.request;

import com.association.gestiondons.entity.MoyenPaiement;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DonRequest {

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être positif")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal montant;

    @NotNull(message = "La date du don est obligatoire")
    private LocalDate dateDon;

    @NotNull(message = "Le moyen de paiement est obligatoire")
    private MoyenPaiement moyen;

    @NotNull(message = "L'identifiant de la campagne est obligatoire")
    private Long campagneId;

    @NotNull(message = "L'identifiant du donateur est obligatoire")
    private Long donateurId;
}
