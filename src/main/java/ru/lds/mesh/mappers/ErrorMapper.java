package ru.lds.mesh.mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.lds.mesh.configs.MapStructConfig;
import ru.lds.mesh.exceptions.AbstractException;
import ru.lds.openapi.model.ErrorResponse;

/** Маппинг сущностей, связанных с ошибками. */
@Mapper(
    config = MapStructConfig.class,
    imports = {LocalDateTime.class, ZoneId.class, DateTimeFormatter.class})
public interface ErrorMapper {

  @Mapping(target = "time", source = "exceptionModel.time")
  @Mapping(target = "code", source = "exceptionModel.exceptionEnum.code")
  @Mapping(target = "message", source = "exceptionModel.exceptionEnum.message")
  @Mapping(target = "info", source = "exceptionModel.info")
  ErrorResponse exceptionToErrorResponse(AbstractException exception);
}
