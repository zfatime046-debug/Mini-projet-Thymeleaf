package com.association.gestiondons.mapper;

import com.association.gestiondons.dto.request.CampagneRequest;
import com.association.gestiondons.dto.response.CampagneResponse;
import com.association.gestiondons.entity.Campagne;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CampagneMapper {

    Campagne toEntity(CampagneRequest request);

    @Mapping(target = "montantCollecte",  ignore = true)
    @Mapping(target = "nombreDons",       ignore = true)
    @Mapping(target = "progression",      ignore = true)
    CampagneResponse toResponse(Campagne campagne);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(CampagneRequest request, @MappingTarget Campagne campagne);
}
