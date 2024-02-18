package ru.lds.mesh.configs;

import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.lds.mesh.exceptions.MapperException;

/** Конфигурация маппинга сущностей. */
@MapperConfig(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unexpectedValueMappingException = MapperException.class)
public interface MapStructConfig {}
