package ru.lds.mesh.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.lds.mesh.configs.MapStructConfig;
import ru.lds.mesh.dtos.EmailDataDto;
import ru.lds.mesh.entities.EmailDataEntity;
import ru.lds.openapi.model.EmailResponse;

/** Маппинг сущностей, связанных с электронной почтой. */
@Mapper(config = MapStructConfig.class)
public interface EmailDataMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userEntity.id", source = "userId")
  EmailDataEntity emailDataDtoToEmailDataEntity(EmailDataDto emailDataDto);

  @Mapping(target = "address", source = "email")
  EmailResponse emailDataEntityToEmailResponse(EmailDataEntity emailDataEntity);

  List<EmailResponse> emailDataEntityToEmailResponse(List<EmailDataEntity> emailDataEntityList);
}
