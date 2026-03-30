package com.association.gestiondons.config;

import com.association.gestiondons.entity.*;
import com.association.gestiondons.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Charge des données de démonstration au démarrage (profil "dev").
 * Les données SQL (data.sql) sont utilisées sinon.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CampagneRepository  campagneRepository;
    private final DonateurRepository  donateurRepository;
    private final DonRepository       donRepository;

    @Override
    public void run(String... args) {
        if (campagneRepository.count() > 0) {
            log.info("Données déjà présentes — initialisation ignorée.");
            return;
        }

        log.info("=== Chargement des données de démonstration ===");

        // Campagnes
        Campagne c1 = campagneRepository.save(Campagne.builder()
                .titre("Aide aux réfugiés 2025")
                .objectif(new BigDecimal("500000"))
                .debut(LocalDate.of(2025, 1, 1))
                .fin(LocalDate.of(2025, 12, 31))
                .statut(StatutCampagne.ACTIVE)
                .build());

        Campagne c2 = campagneRepository.save(Campagne.builder()
                .titre("École pour tous")
                .objectif(new BigDecimal("200000"))
                .debut(LocalDate.of(2025, 3, 1))
                .fin(LocalDate.of(2025, 9, 30))
                .statut(StatutCampagne.ACTIVE)
                .build());

        Campagne c3 = campagneRepository.save(Campagne.builder()
                .titre("Santé communautaire")
                .objectif(new BigDecimal("150000"))
                .debut(LocalDate.of(2024, 6, 1))
                .fin(LocalDate.of(2024, 12, 31))
                .statut(StatutCampagne.TERMINEE)
                .build());

        // Donateurs
        Donateur d1 = donateurRepository.save(Donateur.builder()
                .nom("Ahmed Benali").email("ahmed@example.com")
                .type(TypeDonateur.PARTICULIER).build());

        Donateur d2 = donateurRepository.save(Donateur.builder()
                .nom("Société Atlas SA").email("contact@atlas.ma")
                .type(TypeDonateur.ENTREPRISE).build());

        Donateur d3 = donateurRepository.save(Donateur.builder()
                .nom("Fatima Zahra").email("fz@example.com")
                .type(TypeDonateur.PARTICULIER).build());

        Donateur d4 = donateurRepository.save(Donateur.builder()
                .nom("ONG Solidarité").email("info@solidarite.ma")
                .type(TypeDonateur.ASSOCIATION).build());

        // Dons
        donRepository.save(Don.builder().montant(new BigDecimal("5000"))
                .dateDon(LocalDate.of(2025, 2, 10)).moyen(MoyenPaiement.VIREMENT)
                .campagne(c1).donateur(d2).build());

        donRepository.save(Don.builder().montant(new BigDecimal("1500"))
                .dateDon(LocalDate.of(2025, 2, 15)).moyen(MoyenPaiement.CARTE)
                .campagne(c1).donateur(d1).build());

        donRepository.save(Don.builder().montant(new BigDecimal("3000"))
                .dateDon(LocalDate.of(2025, 3, 5)).moyen(MoyenPaiement.CASH)
                .campagne(c2).donateur(d3).build());

        donRepository.save(Don.builder().montant(new BigDecimal("10000"))
                .dateDon(LocalDate.of(2025, 3, 10)).moyen(MoyenPaiement.VIREMENT)
                .campagne(c1).donateur(d4).build());

        donRepository.save(Don.builder().montant(new BigDecimal("800"))
                .dateDon(LocalDate.of(2025, 3, 20)).moyen(MoyenPaiement.CHEQUE)
                .campagne(c2).donateur(d1).build());

        donRepository.save(Don.builder().montant(new BigDecimal("25000"))
                .dateDon(LocalDate.of(2024, 9, 1)).moyen(MoyenPaiement.VIREMENT)
                .campagne(c3).donateur(d2).build());

        log.info("=== {} campagnes, {} donateurs, {} dons chargés ===",
                campagneRepository.count(), donateurRepository.count(), donRepository.count());
    }
}
