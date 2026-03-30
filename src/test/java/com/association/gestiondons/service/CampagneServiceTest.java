package com.association.gestiondons.service;

import com.association.gestiondons.dto.request.CampagneRequest;
import com.association.gestiondons.dto.response.CampagneResponse;
import com.association.gestiondons.entity.Campagne;
import com.association.gestiondons.entity.StatutCampagne;
import com.association.gestiondons.exception.BusinessException;
import com.association.gestiondons.exception.ResourceNotFoundException;
import com.association.gestiondons.mapper.CampagneMapper;
import com.association.gestiondons.repository.CampagneRepository;
import com.association.gestiondons.service.impl.CampagneServiceImpl;
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
@DisplayName("Tests CampagneService")
class CampagneServiceTest {

    @Mock private CampagneRepository campagneRepository;
    @Mock private CampagneMapper     campagneMapper;

    @InjectMocks private CampagneServiceImpl campagneService;

    private Campagne campagne;
    private CampagneResponse campagneResponse;
    private CampagneRequest  campagneRequest;

    @BeforeEach
    void setUp() {
        campagne = Campagne.builder()
                .id(1L).titre("Aide réfugiés")
                .objectif(new BigDecimal("100000"))
                .debut(LocalDate.now())
                .fin(LocalDate.now().plusMonths(6))
                .statut(StatutCampagne.ACTIVE)
                .build();

        campagneResponse = new CampagneResponse();
        campagneResponse.setId(1L);
        campagneResponse.setTitre("Aide réfugiés");

        campagneRequest = new CampagneRequest();
        campagneRequest.setTitre("Aide réfugiés");
        campagneRequest.setObjectif(new BigDecimal("100000"));
        campagneRequest.setDebut(LocalDate.now());
        campagneRequest.setFin(LocalDate.now().plusMonths(6));
        campagneRequest.setStatut(StatutCampagne.ACTIVE);
    }

    @Test
    @DisplayName("findAll() retourne la liste de toutes les campagnes")
    void findAll_shouldReturnAllCampaigns() {
        when(campagneRepository.findAll()).thenReturn(List.of(campagne));
        when(campagneMapper.toResponse(campagne)).thenReturn(campagneResponse);
        when(campagneRepository.sumMontantByCampagneId(anyLong())).thenReturn(BigDecimal.ZERO);
        when(campagneRepository.countDonsByCampagneId(anyLong())).thenReturn(0L);

        List<CampagneResponse> result = campagneService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).isEqualTo("Aide réfugiés");
        verify(campagneRepository).findAll();
    }

    @Test
    @DisplayName("findById() lève ResourceNotFoundException si introuvable")
    void findById_shouldThrowWhenNotFound() {
        when(campagneRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> campagneService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("create() persiste et retourne la campagne créée")
    void create_shouldPersistCampaign() {
        when(campagneMapper.toEntity(campagneRequest)).thenReturn(campagne);
        when(campagneRepository.save(campagne)).thenReturn(campagne);
        when(campagneMapper.toResponse(campagne)).thenReturn(campagneResponse);
        when(campagneRepository.sumMontantByCampagneId(anyLong())).thenReturn(BigDecimal.ZERO);
        when(campagneRepository.countDonsByCampagneId(anyLong())).thenReturn(0L);

        CampagneResponse result = campagneService.create(campagneRequest);

        assertThat(result).isNotNull();
        verify(campagneRepository).save(any(Campagne.class));
    }

    @Test
    @DisplayName("create() lève BusinessException si fin < debut")
    void create_shouldThrowWhenEndBeforeStart() {
        campagneRequest.setFin(LocalDate.now().minusDays(1));

        assertThatThrownBy(() -> campagneService.create(campagneRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("date de fin");
    }

    @Test
    @DisplayName("delete() lève BusinessException si la campagne a des dons")
    void delete_shouldThrowWhenCampaignHasDons() {
        when(campagneRepository.existsById(1L)).thenReturn(true);
        when(campagneRepository.countDonsByCampagneId(1L)).thenReturn(3L);

        assertThatThrownBy(() -> campagneService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("don(s)");
    }

    @Test
    @DisplayName("delete() supprime si aucun don associé")
    void delete_shouldDeleteWhenNoDons() {
        when(campagneRepository.existsById(1L)).thenReturn(true);
        when(campagneRepository.countDonsByCampagneId(1L)).thenReturn(0L);

        campagneService.delete(1L);

        verify(campagneRepository).deleteById(1L);
    }
}
