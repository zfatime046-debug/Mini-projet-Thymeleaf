package com.association.gestiondons.dto.request;

import com.association.gestiondons.entity.TypeDonateur;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DonateurRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    @Size(max = 100)
    private String email;

    @NotNull(message = "Le type est obligatoire")
    private TypeDonateur type;
}
