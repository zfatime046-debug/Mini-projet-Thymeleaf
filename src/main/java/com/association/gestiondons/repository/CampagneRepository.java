package com.association.gestiondons.repository;

import com.association.gestiondons.entity.Campagne;
import com.association.gestiondons.entity.StatutCampagne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampagneRepository extends JpaRepository<Campagne, Long> {

    List<Campagne> findByStatut(StatutCampagne statut);

    List<Campagne> findByTitreContainingIgnoreCase(String titre);

    @Query("SELECT c FROM Campagne c LEFT JOIN FETCH c.dons WHERE c.id = :id")
    java.util.Optional<Campagne> findByIdWithDons(Long id);

    @Query("SELECT COUNT(d) FROM Don d WHERE d.campagne.id = :campagneId")
    long countDonsByCampagneId(Long campagneId);

    @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Don d WHERE d.campagne.id = :campagneId")
    java.math.BigDecimal sumMontantByCampagneId(Long campagneId);
}
