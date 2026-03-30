package com.association.gestiondons.dto.request;

import com.association.gestiondons.entity.StatutCampagne;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CampagneRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 200, message = "Le titre ne doit pas dépasser 200 caractères")
    private String titre;

    @NotNull(message = "L'objectif financier est obligatoire")
    @DecimalMin(value = "0.01", message = "L'objectif doit être positif")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal objectif;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate debut;

    private LocalDate fin;

    @NotNull(message = "Le statut est obligatoire")
    private StatutCampagne statut;
}
