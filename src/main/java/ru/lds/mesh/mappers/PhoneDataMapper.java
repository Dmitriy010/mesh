package ru.lds.mesh.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.lds.mesh.configs.MapStructConfig;
import ru.lds.mesh.dtos.PhoneDataDto;
import ru.lds.mesh.entities.PhoneDataEntity;
import ru.lds.openapi.model.PhoneResponse;


/** Маппинг сущностей, связанных с телефоном. */
@Mapper(config = MapStructConfig.class)
public interface PhoneDataMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userEntity.id", source = "userId")
  PhoneDataEntity phoneDataDtoToPhoneDataEntity(PhoneDataDto phoneDataDto);

  @Mapping(target = "number", source = "phone")
  PhoneResponse phoneDataEntityToPhoneResponse(PhoneDataEntity phoneDataEntity);

  List<PhoneResponse> phoneDataEntityToPhoneResponse(List<PhoneDataEntity> phoneDataEntityList);
}
