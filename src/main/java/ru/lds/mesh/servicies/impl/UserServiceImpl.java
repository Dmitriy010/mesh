package ru.lds.mesh.servicies.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.lds.mesh.entities.UserEntity;
import ru.lds.mesh.exceptions.ExceptionEnum;
import ru.lds.mesh.exceptions.ExceptionModel;
import ru.lds.mesh.exceptions.NotFoundException;
import ru.lds.mesh.mappers.UserMapper;
import ru.lds.mesh.repositories.UserRepository;
import ru.lds.mesh.repositories.specifications.UserSpecification;
import ru.lds.mesh.servicies.UserService;
import ru.lds.mesh.utils.PageableUtils;
import ru.lds.openapi.model.UserFilterRequest;
import ru.lds.openapi.model.UserPageResponse;

/** Реализация сервиса, предоставляющего методы для работы с пользователями. */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

  UserRepository userRepository;

  UserMapper userMapper;

  @Override
  @Cacheable(value = "users", key = "{#userFilter, #pageNumber, #recordsNumber}")
  public UserPageResponse findAllByUserFilterPage(
      UserFilterRequest userFilter, Integer pageNumber, Integer recordsNumber) {
    var userEntityPage =
        userRepository.findAll(
            UserSpecification.getByUserFilter(userFilter),
            PageableUtils.createPageRequest(pageNumber, recordsNumber));

    return userMapper.userEntityPageToUserResponsePage(userEntityPage);
  }

  /**
   * Реализация получения текущего пользователя.
   *
   * @return Объект `UserEntity` с информацией о текущем пользователе.
   * @throws NotFoundException Если текущий пользователь не найден.
   */
  public UserEntity getCurrentUser() {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();

    return userRepository
        .findByEmailDataEntitiesEmailOrPhoneDataEntitiesPhone(username, username)
        .orElseThrow(
            () ->
                new NotFoundException(
                    ExceptionModel.builder()
                        .exceptionEnum(ExceptionEnum.USER_NOT_FOUND)
                        .info("Контекст не найден для пользователя с username: " + username)
                        .build()));
  }

  @Override
  public UserEntity findUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(
            () ->
                new NotFoundException(
                    ExceptionModel.builder().exceptionEnum(ExceptionEnum.USER_NOT_FOUND).build()));
  }

  @Override
  public UserEntity findUserByPhoneId(Long phoneId) {
    return userRepository
        .findByPhoneDataEntitiesId(phoneId)
        .orElseThrow(
            () ->
                new NotFoundException(
                    ExceptionModel.builder().exceptionEnum(ExceptionEnum.USER_NOT_FOUND).build()));
  }

  @Override
  public UserEntity findUserByEmailId(Long emailId) {
    return userRepository
        .findByEmailDataEntitiesId(emailId)
        .orElseThrow(
            () ->
                new NotFoundException(
                    ExceptionModel.builder().exceptionEnum(ExceptionEnum.USER_NOT_FOUND).build()));
  }
}
