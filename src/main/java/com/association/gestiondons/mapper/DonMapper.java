package com.association.gestiondons.mapper;

import com.association.gestiondons.dto.request.DonRequest;
import com.association.gestiondons.dto.response.DonResponse;
import com.association.gestiondons.entity.Don;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DonMapper {

    @Mapping(target = "campagne",  ignore = true)
    @Mapping(target = "donateur",  ignore = true)
    Don toEntity(DonRequest request);

    @Mapping(target = "campagneId",    source = "campagne.id")
    @Mapping(target = "campagneTitre", source = "campagne.titre")
    @Mapping(target = "donateurId",    source = "donateur.id")
    @Mapping(target = "donateurNom",   source = "donateur.nom")
    @Mapping(target = "donateurEmail", source = "donateur.email")
    DonResponse toResponse(Don don);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "campagne", ignore = true)
    @Mapping(target = "donateur", ignore = true)
    void updateFromRequest(DonRequest request, @MappingTarget Don don);
}
