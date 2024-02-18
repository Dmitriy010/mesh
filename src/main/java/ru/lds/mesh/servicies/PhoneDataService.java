package ru.lds.mesh.servicies;

import java.util.List;
import ru.lds.openapi.model.PhoneResponse;

/** Сервис, предоставляющий методы для работы с данными о телефонах. */
public interface PhoneDataService {

  /**
   * Добавляет телефон по идентификатору пользователя.
   *
   * @param userId Идентификатор пользователя
   * @param number Номер телефона
   * @return Список ответов с информацией о телефонах
   */
  List<PhoneResponse> addPhoneByUserId(Long userId, String number);

  /**
   * Удаляет телефон по идентификатору.
   *
   * @param phoneId Идентификатор телефона
   * @return Список ответов с информацией о телефонах
   */
  List<PhoneResponse> deletePhoneById(Long phoneId);

  /**
   * Обновляет телефон по идентификатору.
   *
   * @param phoneId Идентификатор телефона
   * @param number Номер телефона
   * @return Список ответов с информацией о телефонах
   */
  List<PhoneResponse> updatePhoneById(Long phoneId, String number);
}
