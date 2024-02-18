package ru.lds.mesh.servicies;

import ru.lds.openapi.model.JwtTokenResponse;
import ru.lds.openapi.model.SignInRequest;

/** Сервис, предоставляющий методы для аутентификации пользователя. */
public interface AuthenticationService {

  /**
   * Метод для аутентификации пользователя.
   *
   * @param signInRequest объект, содержащий логин и пароль пользователя.
   * @return объект, содержащий сгенерированный JWT-токен.
   */
  JwtTokenResponse signIn(SignInRequest signInRequest);

  /**
   * Получение идентификатора текущего пользователя из JWT-токена.
   *
   * @return Идентификатор текущего пользователя.
   */
  Long getCurrentUserIdFromJwtToken();
}
