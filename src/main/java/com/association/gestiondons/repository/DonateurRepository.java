package com.association.gestiondons.repository;

import com.association.gestiondons.entity.Donateur;
import com.association.gestiondons.entity.TypeDonateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonateurRepository extends JpaRepository<Donateur, Long> {

    Optional<Donateur> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Donateur> findByType(TypeDonateur type);

    List<Donateur> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT d FROM Donateur d WHERE d.type = :type ORDER BY d.nom")
    List<Donateur> findByTypeOrderByNom(TypeDonateur type);

    @Query("SELECT COALESCE(SUM(dn.montant), 0) FROM Don dn WHERE dn.donateur.id = :donateurId")
    java.math.BigDecimal sumMontantByDonateurId(Long donateurId);
}
