package ru.lds.mesh.servicies;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.lds.mesh.security.CustomUser;

/**
 * Сервис, предоставляющий методы для получения информации о пользователе с расширенными данными.
 */
public interface CustomUserDetailsService extends UserDetailsService {

  /**
   * Метод для поиска пользователя по логину.
   *
   * @param login логин пользователя(почта или телефон).
   * @return объект с расширенной информацией о пользователе.
   */
  CustomUser findByLogin(String login);
}
