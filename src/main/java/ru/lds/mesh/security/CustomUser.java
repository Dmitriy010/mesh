package ru.lds.mesh.security;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/** Пользователь с расширенной информацией. */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomUser extends User {

  String login;
  Long id;

  /**
   * Создает объект CustomUser.
   *
   * @param id Идентификатор пользователя.
   * @param login Логин пользователя.
   * @param password Пароль пользователя.
   * @param authorities Коллекция прав доступа пользователя.
   */
  public CustomUser(
      Long id, String login, String password, Collection<? extends GrantedAuthority> authorities) {
    super(login, password, authorities);
    this.login = login;
    this.id = id;
  }
}
