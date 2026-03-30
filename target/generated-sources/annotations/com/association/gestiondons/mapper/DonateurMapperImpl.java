package com.association.gestiondons.mapper;

import com.association.gestiondons.dto.request.DonateurRequest;
import com.association.gestiondons.dto.response.DonateurResponse;
import com.association.gestiondons.entity.Donateur;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T14:04:16+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.18 (Microsoft)"
)
@Component
public class DonateurMapperImpl implements DonateurMapper {

    @Override
    public Donateur toEntity(DonateurRequest request) {
        if ( request == null ) {
            return null;
        }

        Donateur.DonateurBuilder donateur = Donateur.builder();

        donateur.nom( request.getNom() );
        donateur.email( request.getEmail() );
        donateur.type( request.getType() );

        return donateur.build();
    }

    @Override
    public DonateurResponse toResponse(Donateur donateur) {
        if ( donateur == null ) {
            return null;
        }

        DonateurResponse donateurResponse = new DonateurResponse();

        donateurResponse.setId( donateur.getId() );
        donateurResponse.setNom( donateur.getNom() );
        donateurResponse.setEmail( donateur.getEmail() );
        donateurResponse.setType( donateur.getType() );

        return donateurResponse;
    }

    @Override
    public void updateFromRequest(DonateurRequest request, Donateur donateur) {
        if ( request == null ) {
            return;
        }

        if ( request.getNom() != null ) {
            donateur.setNom( request.getNom() );
        }
        if ( request.getEmail() != null ) {
            donateur.setEmail( request.getEmail() );
        }
        if ( request.getType() != null ) {
            donateur.setType( request.getType() );
        }
    }
}
