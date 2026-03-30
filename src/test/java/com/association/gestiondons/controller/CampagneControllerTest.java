package com.association.gestiondons.controller;

import com.association.gestiondons.dto.request.CampagneRequest;
import com.association.gestiondons.dto.response.CampagneResponse;
import com.association.gestiondons.entity.StatutCampagne;
import com.association.gestiondons.exception.GlobalExceptionHandler;
import com.association.gestiondons.exception.ResourceNotFoundException;
import com.association.gestiondons.service.CampagneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests CampagneController")
class CampagneControllerTest {

    @Mock private CampagneService campagneService;
    @InjectMocks private CampagneController campagneController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CampagneResponse campagneResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(campagneController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        campagneResponse = new CampagneResponse();
        campagneResponse.setId(1L);
        campagneResponse.setTitre("Aide réfugiés");
        campagneResponse.setObjectif(new BigDecimal("100000"));
        campagneResponse.setStatut(StatutCampagne.ACTIVE);
        campagneResponse.setMontantCollecte(BigDecimal.ZERO);
        campagneResponse.setProgression(0.0);
    }

    @Test
    @DisplayName("GET /api/campagnes → 200 avec liste")
    void getAll_shouldReturn200() throws Exception {
        when(campagneService.findAll()).thenReturn(List.of(campagneResponse));

        mockMvc.perform(get("/api/campagnes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].titre").value("Aide réfugiés"));
    }

    @Test
    @DisplayName("GET /api/campagnes/{id} → 200")
    void getById_shouldReturn200() throws Exception {
        when(campagneService.findById(1L)).thenReturn(campagneResponse);

        mockMvc.perform(get("/api/campagnes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @DisplayName("GET /api/campagnes/{id} introuvable → 404")
    void getById_shouldReturn404WhenNotFound() throws Exception {
        when(campagneService.findById(99L)).thenThrow(new ResourceNotFoundException("Campagne", 99L));

        mockMvc.perform(get("/api/campagnes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("POST /api/campagnes valide → 201")
    void create_shouldReturn201() throws Exception {
        CampagneRequest request = new CampagneRequest();
        request.setTitre("Nouvelle campagne");
        request.setObjectif(new BigDecimal("50000"));
        request.setDebut(LocalDate.now());
        request.setFin(LocalDate.now().plusMonths(3));
        request.setStatut(StatutCampagne.ACTIVE);

        when(campagneService.create(any())).thenReturn(campagneResponse);

        mockMvc.perform(post("/api/campagnes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("POST /api/campagnes sans titre → 400")
    void create_shouldReturn400WhenTitleMissing() throws Exception {
        CampagneRequest request = new CampagneRequest();
        request.setObjectif(new BigDecimal("50000"));
        request.setDebut(LocalDate.now());
        request.setStatut(StatutCampagne.ACTIVE);

        mockMvc.perform(post("/api/campagnes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/campagnes/{id} → 200")
    void delete_shouldReturn200() throws Exception {
        doNothing().when(campagneService).delete(1L);

        mockMvc.perform(delete("/api/campagnes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
