package ru.lds.mesh.servicies;

import ru.lds.mesh.entities.UserEntity;
import ru.lds.openapi.model.UserFilterRequest;
import ru.lds.openapi.model.UserPageResponse;

/** Сервис, предоставляющий методы для работы с пользователями. */
public interface UserService {

  /**
   * Возвращает список пользователей с применением фильтра и пагинацией.
   *
   * @param userFilter Фильтр пользователей
   * @param pageNumber Номер страницы
   * @param recordsNumber Количество записей на странице
   * @return Страница с информацией о пользователях
   */
  UserPageResponse findAllByUserFilterPage(
      UserFilterRequest userFilter, Integer pageNumber, Integer recordsNumber);

  /**
   * Получение текущего пользователя.
   *
   * @return Найденный пользователь
   */
  UserEntity getCurrentUser();

  /**
   * Находит пользователя по указанному идентификатору.
   *
   * @param id Идентификатор пользователя
   * @return Найденный пользователь
   */
  UserEntity findUserById(Long id);

  /**
   * Находит пользователя по указанному идентификатору телефона.
   *
   * @param phoneId Идентификатор телефона пользователя
   * @return Найденный пользователь
   */
  UserEntity findUserByPhoneId(Long phoneId);

  /**
   * Находит пользователя по указанному идентификатору электронной почты.
   *
   * @param emailId Идентификатор электронной почты пользователя
   * @return Найденный пользователь
   */
  UserEntity findUserByEmailId(Long emailId);
}
