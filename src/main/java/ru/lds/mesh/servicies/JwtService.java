package ru.lds.mesh.servicies;

import org.springframework.security.core.userdetails.UserDetails;
import ru.lds.mesh.security.CustomUser;

/** Сервис, предоставляющий методы для работы с JWT-токеном. */
public interface JwtService {

  /**
   * Извлечение логина пользователя из токена.
   *
   * @param token токен
   * @return Логин пользователя
   */
  String extractLogin(String token);

  /**
   * Извлечение логина пользователя из токена.
   *
   * @param token токен
   * @param key ключ параметра для извлечения
   * @return Идентификатор пользователя
   */
  Long extractClaimUserId(String token, String key);

  /**
   * Генерация токена.
   *
   * @param customUser данные пользователя
   * @return Токен
   */
  String generateToken(CustomUser customUser);

  /**
   * Проверка токена на валидность.
   *
   * @param token токен
   * @param userDetails данные пользователя
   * @return true, если токен валиден
   */
  boolean isTokenValid(String token, UserDetails userDetails);
}
