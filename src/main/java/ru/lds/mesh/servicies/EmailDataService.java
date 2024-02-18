package ru.lds.mesh.servicies;

import java.util.List;
import ru.lds.openapi.model.EmailResponse;

/** Сервис, предоставляющий методы для работы с электронной почтой. */
public interface EmailDataService {

  /**
   * Добавляет почту по идентификатору пользователя.
   *
   * @param userId Идентификатор пользователя
   * @param email Электронная почта
   * @return Список с информацией о почтах пользователя
   */
  List<EmailResponse> addEmailByUserId(Long userId, String email);

  /**
   * Удаляет почту по идентификатору.
   *
   * @param emailId Идентификатор электронной почты
   * @return Список с информацией о почтах пользователя
   */
  List<EmailResponse> deleteEmailById(Long emailId);

  /**
   * Обновляет почту по идентификатору.
   *
   * @param emailId Идентификатор электронной почты
   * @param email Электронная почта
   * @return Список с информацией о почтах пользователя
   */
  List<EmailResponse> updateEmailById(Long emailId, String email);
}
