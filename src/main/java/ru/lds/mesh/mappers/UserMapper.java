package ru.lds.mesh.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.lds.mesh.configs.MapStructConfig;
import ru.lds.mesh.entities.UserEntity;
import ru.lds.openapi.model.PageResponse;
import ru.lds.openapi.model.UserPageResponse;
import ru.lds.openapi.model.UserResponse;

/** Маппинг сущностей, связанных с пользователем. */
@Mapper(
    config = MapStructConfig.class,
    uses = {PhoneDataMapper.class, EmailDataMapper.class})
public interface UserMapper {

  /**
   * Преобразует страницу объектов UserEntity в страницу объектов UserResponse.
   *
   * @param userEntityPage Страница объектов UserEntity, которую необходимо преобразовать.
   * @return Страница объектов UserResponse, полученная в результате преобразования.
   */
  default UserPageResponse userEntityPageToUserResponsePage(Page<UserEntity> userEntityPage) {
    var userEntityList = userEntityPage.getContent();
    var userResponseList = userBodyToUserEntity(userEntityList);

    return UserPageResponse.builder()
        .users(userResponseList)
        .pageInfo(
            PageResponse.builder()
                .recordsNumber(userEntityPage.getNumberOfElements())
                .pageNumber(userEntityPage.getNumber() + 1)
                .totalElements(userEntityPage.getTotalElements())
                .totalPages(userEntityPage.getTotalPages())
                .build())
        .build();
  }

  @Mapping(target = "emails", source = "emailDataEntities")
  @Mapping(target = "phones", source = "phoneDataEntities")
  @Mapping(target = "balance", source = "accountEntity.balance")
  @Mapping(target = "actualBalance", source = "accountEntity.actualBalance")
  UserResponse userBodyToUserEntity(UserEntity userEntity);

  List<UserResponse> userBodyToUserEntity(List<UserEntity> userEntityListList);
}
