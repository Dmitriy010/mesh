package ru.lds.mesh.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/** Перечисление исключений. */
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ExceptionEnum {
  ACCOUNT_NOT_FOUND(1, "Аккаунт пользователя не найден"),
  ACCOUNT_ACTUAL_BALANCE_NOT_ENOUGH(2, "Не хватает средств для осуществления денежного перевода"),
  USER_NOT_FOUND(3, "Пользователь не найден"),
  EMAIL_EXISTS(4, "Электронная почта занята другим пользователем"),
  EMAIL_NOT_FOUND(5, "Электронная почта не найдена"),
  PHONE_EXISTS(6, "Номер телефона занят другим пользователем"),
  PHONE_NOT_FOUND(7, "Телефон не найдена"),
  PARSE_JWT_TOKEN(8, "Произошла ошибка при парсинге JWT токена"),
  TRANSFER_MONEY(9, "Произошла ошибка при переводе денежных средств"),
  FORBIDDEN(10, "Доступ запрещен"),
  AUTHENTICATION(11, "Ошибка аутентификации"),
  VALIDATION(12, "Данные не прошли валидацию");

  int code;
  String message;
}
