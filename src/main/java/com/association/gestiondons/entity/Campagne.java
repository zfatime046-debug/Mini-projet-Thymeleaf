package com.association.gestiondons.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campagne")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Campagne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal objectif;

    @Column(nullable = false)
    private LocalDate debut;

    @Column
    private LocalDate fin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutCampagne statut;

    @OneToMany(mappedBy = "campagne", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Don> dons = new ArrayList<>();
}
