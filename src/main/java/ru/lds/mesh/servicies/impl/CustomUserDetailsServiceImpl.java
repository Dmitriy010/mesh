package ru.lds.mesh.servicies.impl;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.lds.mesh.exceptions.ConflictException;
import ru.lds.mesh.exceptions.ExceptionEnum;
import ru.lds.mesh.exceptions.ExceptionModel;
import ru.lds.mesh.exceptions.NotFoundException;
import ru.lds.mesh.repositories.UserRepository;
import ru.lds.mesh.security.CustomUser;
import ru.lds.mesh.servicies.CustomUserDetailsService;

/**
 * Реализация сервиса, предоставляющего методы для получения информации о пользователе с
 * расширенными данными.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user =
        userRepository
            .findByEmailDataEntitiesEmailOrPhoneDataEntitiesPhone(username, username)
            .orElseThrow(
                () ->
                    new ConflictException(
                        ExceptionModel.builder()
                            .exceptionEnum(ExceptionEnum.USER_NOT_FOUND)
                            .info("Username: " + username)
                            .build()));

    return new User(username, user.getPassword(), List.of());
  }

  /**
   * Реализация метода поиска пользователя по логину.
   *
   * @param login Логин пользователя.
   * @return Объект CustomUser с информацией о пользователе.
   * @throws NotFoundException Если пользователь не найден.
   */
  public CustomUser findByLogin(String login) {
    var user =
        userRepository
            .findByEmailDataEntitiesEmailOrPhoneDataEntitiesPhone(login, login)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ExceptionModel.builder()
                            .exceptionEnum(ExceptionEnum.USER_NOT_FOUND)
                            .info("Login: " + login)
                            .build()));

    return new CustomUser(user.getId(), login, user.getPassword(), List.of());
  }
}
