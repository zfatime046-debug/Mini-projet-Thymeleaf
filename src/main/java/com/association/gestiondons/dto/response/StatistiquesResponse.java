package com.association.gestiondons.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class StatistiquesResponse {

    private BigDecimal totalCollecte;
    private BigDecimal panierMoyen;
    private long nombreDons;
    private long nombreDonateurs;
    private long nombreCampagnesActives;

    // Total par campagne : {titre, montant, nbDons}
    private List<StatCampagne> parCampagne;

    // Répartition par moyen de paiement
    private Map<String, BigDecimal> parMoyen;

    // Répartition des donateurs par type
    private Map<String, Long> donateurParType;

    @Data
    @Builder
    public static class StatCampagne {
        private String titre;
        private BigDecimal montantCollecte;
        private BigDecimal objectif;
        private double progression;
        private long nombreDons;
    }
}
