package ru.lds.mesh.servicies.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.lds.mesh.servicies.AuthenticationService;
import ru.lds.mesh.servicies.CustomUserDetailsService;
import ru.lds.openapi.model.JwtTokenResponse;
import ru.lds.openapi.model.SignInRequest;

/**
 * Реализация сервиса, предоставляющего методы аутентификации пользователей и генерации JWT-токенов.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private static final String BEARER_PREFIX = "Bearer ";
  private static final String HEADER_NAME = "Authorization";

  @Value("${jwt.claim_key}")
  private String claimKey;

  private final JwtServiceImpl jwtService;
  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailsService customUserDetailsService;

  @Override
  public JwtTokenResponse signIn(SignInRequest signInRequest) {
    log.debug("Аутентификация пользователя, login: {}", signInRequest.getLogin());
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            signInRequest.getLogin(), signInRequest.getPassword()));

    log.debug("Поиск пользователя, login: {}", signInRequest.getLogin());
    var customUser = customUserDetailsService.findByLogin(signInRequest.getLogin());

    log.debug(
        "Генерация JWT-токена, login: {}, userId: {}", customUser.getLogin(), customUser.getId());
    var jwtToken = jwtService.generateToken(customUser);

    return JwtTokenResponse.builder().jwtToken(jwtToken).build();
  }

  @Override
  public Long getCurrentUserIdFromJwtToken() {
    var request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    var authHeader = request.getHeader(HEADER_NAME);
    var jwtToken = authHeader.substring(BEARER_PREFIX.length());

    return jwtService.extractClaimUserId(jwtToken, claimKey);
  }
}
