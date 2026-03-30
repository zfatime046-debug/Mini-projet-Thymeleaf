package com.association.gestiondons.dto.request;

import com.association.gestiondons.entity.MoyenPaiement;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DonFilterRequest {
    private Long campagneId;
    private MoyenPaiement moyen;
    private LocalDate debut;
    private LocalDate fin;
}
