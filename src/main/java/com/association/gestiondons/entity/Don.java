package com.association.gestiondons.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "don")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Don {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_don", nullable = false)
    private LocalDate dateDon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MoyenPaiement moyen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campagne_id", nullable = false)
    private Campagne campagne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donateur_id", nullable = false)
    private Donateur donateur;
}
