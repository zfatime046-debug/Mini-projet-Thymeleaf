package com.association.gestiondons.mapper;

import com.association.gestiondons.dto.request.CampagneRequest;
import com.association.gestiondons.dto.response.CampagneResponse;
import com.association.gestiondons.entity.Campagne;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T14:04:15+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.18 (Microsoft)"
)
@Component
public class CampagneMapperImpl implements CampagneMapper {

    @Override
    public Campagne toEntity(CampagneRequest request) {
        if ( request == null ) {
            return null;
        }

        Campagne.CampagneBuilder campagne = Campagne.builder();

        campagne.titre( request.getTitre() );
        campagne.objectif( request.getObjectif() );
        campagne.debut( request.getDebut() );
        campagne.fin( request.getFin() );
        campagne.statut( request.getStatut() );

        return campagne.build();
    }

    @Override
    public CampagneResponse toResponse(Campagne campagne) {
        if ( campagne == null ) {
            return null;
        }

        CampagneResponse campagneResponse = new CampagneResponse();

        campagneResponse.setId( campagne.getId() );
        campagneResponse.setTitre( campagne.getTitre() );
        campagneResponse.setObjectif( campagne.getObjectif() );
        campagneResponse.setDebut( campagne.getDebut() );
        campagneResponse.setFin( campagne.getFin() );
        campagneResponse.setStatut( campagne.getStatut() );

        return campagneResponse;
    }

    @Override
    public void updateFromRequest(CampagneRequest request, Campagne campagne) {
        if ( request == null ) {
            return;
        }

        if ( request.getTitre() != null ) {
            campagne.setTitre( request.getTitre() );
        }
        if ( request.getObjectif() != null ) {
            campagne.setObjectif( request.getObjectif() );
        }
        if ( request.getDebut() != null ) {
            campagne.setDebut( request.getDebut() );
        }
        if ( request.getFin() != null ) {
            campagne.setFin( request.getFin() );
        }
        if ( request.getStatut() != null ) {
            campagne.setStatut( request.getStatut() );
        }
    }
}
