package com.association.gestiondons.service;

import com.association.gestiondons.dto.request.DonRequest;
import com.association.gestiondons.dto.response.DonResponse;
import com.association.gestiondons.entity.*;
import com.association.gestiondons.exception.ResourceNotFoundException;
import com.association.gestiondons.mapper.DonMapper;
import com.association.gestiondons.repository.CampagneRepository;
import com.association.gestiondons.repository.DonateurRepository;
import com.association.gestiondons.repository.DonRepository;
import com.association.gestiondons.service.impl.DonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests DonService")
class DonServiceTest {

    @Mock private DonRepository      donRepository;
    @Mock private CampagneRepository campagneRepository;
    @Mock private DonateurRepository donateurRepository;
    @Mock private DonMapper          donMapper;

    @InjectMocks private DonServiceImpl donService;

    private Don don;
    private DonResponse donResponse;
    private DonRequest  donRequest;
    private Campagne    campagne;
    private Donateur    donateur;

    @BeforeEach
    void setUp() {
        campagne = Campagne.builder().id(1L).titre("Campagne test")
                .objectif(new BigDecimal("50000")).statut(StatutCampagne.ACTIVE)
                .debut(LocalDate.now()).build();

        donateur = Donateur.builder().id(1L).nom("Test User")
                .email("test@example.com").type(TypeDonateur.PARTICULIER).build();

        don = Don.builder().id(1L).montant(new BigDecimal("2000"))
                .dateDon(LocalDate.now()).moyen(MoyenPaiement.VIREMENT)
                .campagne(campagne).donateur(donateur).build();

        donResponse = new DonResponse();
        donResponse.setId(1L);
        donResponse.setMontant(new BigDecimal("2000"));

        donRequest = new DonRequest();
        donRequest.setMontant(new BigDecimal("2000"));
        donRequest.setDateDon(LocalDate.now());
        donRequest.setMoyen(MoyenPaiement.VIREMENT);
        donRequest.setCampagneId(1L);
        donRequest.setDonateurId(1L);
    }

    @Test
    @DisplayName("findAll() retourne tous les dons")
    void findAll_shouldReturnAllDons() {
        when(donRepository.findAll()).thenReturn(List.of(don));
        when(donMapper.toResponse(don)).thenReturn(donResponse);

        List<DonResponse> result = donService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMontant()).isEqualByComparingTo("2000");
    }

    @Test
    @DisplayName("create() lie correctement campagne et donateur")
    void create_shouldLinkCampaignAndDonor() {
        when(campagneRepository.findById(1L)).thenReturn(Optional.of(campagne));
        when(donateurRepository.findById(1L)).thenReturn(Optional.of(donateur));
        when(donMapper.toEntity(donRequest)).thenReturn(don);
        when(donRepository.save(don)).thenReturn(don);
        when(donMapper.toResponse(don)).thenReturn(donResponse);

        DonResponse result = donService.create(donRequest);

        assertThat(result).isNotNull();
        verify(donRepository).save(don);
        verify(campagneRepository).findById(1L);
        verify(donateurRepository).findById(1L);
    }

    @Test
    @DisplayName("create() lève ResourceNotFoundException si campagne absente")
    void create_shouldThrowWhenCampaignNotFound() {
        when(campagneRepository.findById(99L)).thenReturn(Optional.empty());
        donRequest.setCampagneId(99L);

        assertThatThrownBy(() -> donService.create(donRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("delete() supprime le don existant")
    void delete_shouldDeleteExistingDon() {
        when(donRepository.existsById(1L)).thenReturn(true);

        donService.delete(1L);

        verify(donRepository).deleteById(1L);
    }

    @Test
    @DisplayName("delete() lève ResourceNotFoundException si don absent")
    void delete_shouldThrowWhenNotFound() {
        when(donRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> donService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
