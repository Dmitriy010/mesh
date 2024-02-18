package ru.lds.mesh.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.lds.mesh.servicies.AuthenticationService;
import ru.lds.openapi.api.AuthApi;
import ru.lds.openapi.model.JwtTokenResponse;
import ru.lds.openapi.model.SignInRequest;

/** Реализация API для методов аутентификации. */
@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthApiImpl implements AuthApi {

  AuthenticationService authenticationService;

  @Override
  public ResponseEntity<JwtTokenResponse> signIn(SignInRequest signInRequest) {
    log.info("Запрос на аутентификацию пользователя, login: {}", signInRequest.getLogin());
    return ResponseEntity.ok(authenticationService.signIn(signInRequest));
  }
}
