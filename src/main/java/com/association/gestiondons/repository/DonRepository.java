package com.association.gestiondons.repository;

import com.association.gestiondons.entity.Don;
import com.association.gestiondons.entity.MoyenPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonRepository extends JpaRepository<Don, Long>, JpaSpecificationExecutor<Don> {

    // ── Filtres simples ──────────────────────────────────────────
    List<Don> findByCampagneId(Long campagneId);

    List<Don> findByDonateurId(Long donateurId);

    List<Don> findByMoyen(MoyenPaiement moyen);

    List<Don> findByDateDonBetween(LocalDate debut, LocalDate fin);

    // ── Filtres combinés ─────────────────────────────────────────
    List<Don> findByCampagneIdAndMoyen(Long campagneId, MoyenPaiement moyen);

    @Query("""
        SELECT d FROM Don d
        JOIN FETCH d.donateur
        JOIN FETCH d.campagne
        WHERE (:campagneId IS NULL OR d.campagne.id = :campagneId)
          AND (:moyen      IS NULL OR d.moyen = :moyen)
          AND (:debut      IS NULL OR d.dateDon >= :debut)
          AND (:fin        IS NULL OR d.dateDon <= :fin)
        ORDER BY d.dateDon DESC
    """)
    List<Don> findWithFilters(
        @Param("campagneId") Long campagneId,
        @Param("moyen")      MoyenPaiement moyen,
        @Param("debut")      LocalDate debut,
        @Param("fin")        LocalDate fin
    );

    // ── Statistiques ─────────────────────────────────────────────
    @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Don d")
    BigDecimal totalMontant();

    @Query("SELECT COALESCE(AVG(d.montant), 0) FROM Don d")
    BigDecimal panierMoyen();

    @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Don d WHERE d.campagne.id = :campagneId")
    BigDecimal totalMontantByCampagne(@Param("campagneId") Long campagneId);

    @Query("""
        SELECT d.moyen, COALESCE(SUM(d.montant), 0)
        FROM Don d
        GROUP BY d.moyen
        ORDER BY SUM(d.montant) DESC
    """)
    List<Object[]> statsParMoyen();

    @Query("""
        SELECT c.titre, COALESCE(SUM(d.montant), 0), COUNT(d)
        FROM Don d JOIN d.campagne c
        GROUP BY c.id, c.titre
        ORDER BY SUM(d.montant) DESC
    """)
    List<Object[]> statsParCampagne();
}
