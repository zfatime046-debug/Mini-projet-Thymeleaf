package com.association.gestiondons.mapper;

import com.association.gestiondons.dto.request.DonateurRequest;
import com.association.gestiondons.dto.response.DonateurResponse;
import com.association.gestiondons.entity.Donateur;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DonateurMapper {

    Donateur toEntity(DonateurRequest request);

    @Mapping(target = "totalDonne",  ignore = true)
    @Mapping(target = "nombreDons",  ignore = true)
    DonateurResponse toResponse(Donateur donateur);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(DonateurRequest request, @MappingTarget Donateur donateur);
}
