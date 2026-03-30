package com.association.gestiondons.mapper;

import com.association.gestiondons.dto.request.DonRequest;
import com.association.gestiondons.dto.response.DonResponse;
import com.association.gestiondons.entity.Campagne;
import com.association.gestiondons.entity.Don;
import com.association.gestiondons.entity.Donateur;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T14:04:15+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.18 (Microsoft)"
)
@Component
public class DonMapperImpl implements DonMapper {

    @Override
    public Don toEntity(DonRequest request) {
        if ( request == null ) {
            return null;
        }

        Don.DonBuilder don = Don.builder();

        don.montant( request.getMontant() );
        don.dateDon( request.getDateDon() );
        don.moyen( request.getMoyen() );

        return don.build();
    }

    @Override
    public DonResponse toResponse(Don don) {
        if ( don == null ) {
            return null;
        }

        DonResponse donResponse = new DonResponse();

        donResponse.setCampagneId( donCampagneId( don ) );
        donResponse.setCampagneTitre( donCampagneTitre( don ) );
        donResponse.setDonateurId( donDonateurId( don ) );
        donResponse.setDonateurNom( donDonateurNom( don ) );
        donResponse.setDonateurEmail( donDonateurEmail( don ) );
        donResponse.setId( don.getId() );
        donResponse.setMontant( don.getMontant() );
        donResponse.setDateDon( don.getDateDon() );
        donResponse.setMoyen( don.getMoyen() );

        return donResponse;
    }

    @Override
    public void updateFromRequest(DonRequest request, Don don) {
        if ( request == null ) {
            return;
        }

        if ( request.getMontant() != null ) {
            don.setMontant( request.getMontant() );
        }
        if ( request.getDateDon() != null ) {
            don.setDateDon( request.getDateDon() );
        }
        if ( request.getMoyen() != null ) {
            don.setMoyen( request.getMoyen() );
        }
    }

    private Long donCampagneId(Don don) {
        if ( don == null ) {
            return null;
        }
        Campagne campagne = don.getCampagne();
        if ( campagne == null ) {
            return null;
        }
        Long id = campagne.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String donCampagneTitre(Don don) {
        if ( don == null ) {
            return null;
        }
        Campagne campagne = don.getCampagne();
        if ( campagne == null ) {
            return null;
        }
        String titre = campagne.getTitre();
        if ( titre == null ) {
            return null;
        }
        return titre;
    }

    private Long donDonateurId(Don don) {
        if ( don == null ) {
            return null;
        }
        Donateur donateur = don.getDonateur();
        if ( donateur == null ) {
            return null;
        }
        Long id = donateur.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String donDonateurNom(Don don) {
        if ( don == null ) {
            return null;
        }
        Donateur donateur = don.getDonateur();
        if ( donateur == null ) {
            return null;
        }
        String nom = donateur.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    private String donDonateurEmail(Don don) {
        if ( don == null ) {
            return null;
        }
        Donateur donateur = don.getDonateur();
        if ( donateur == null ) {
            return null;
        }
        String email = donateur.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }
}
