package ru.lds.mesh.servicies.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.lds.mesh.exceptions.ExceptionEnum;
import ru.lds.mesh.exceptions.ExceptionModel;
import ru.lds.mesh.exceptions.JwtTokenException;
import ru.lds.mesh.security.CustomUser;
import ru.lds.mesh.servicies.JwtService;

/** Реализация сервиса, предоставляющего методы для работы с JWT-токеном. */
@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private Long jwtExpiration;

  @Value("${jwt.claim_key}")
  private String claimKey;

  @Override
  public String extractLogin(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public Long extractClaimUserId(String token, String key) {
    return extractClaim(token, claims -> claims.get(key, Long.class));
  }

  @Override
  public String generateToken(CustomUser customUser) {
    var claims = new HashMap<String, Object>();
    claims.put(claimKey, customUser.getId());

    return generateToken(claims, customUser);
  }

  /**
   * Генерация токена.
   *
   * @param extraClaims дополнительные данные
   * @param customUser данные пользователя
   * @return токен
   */
  private String generateToken(Map<String, Object> extraClaims, CustomUser customUser) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(customUser.getLogin())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final var login = extractLogin(token);

    return (login.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  /**
   * Извлечение данных из токена.
   *
   * @param token токен
   * @param claimsResolvers функция извлечения данных
   * @param <T> тип данных
   * @return данные
   */
  private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
    final var claims = extractAllClaims(token);
    return claimsResolvers.apply(claims);
  }

  /**
   * Проверка токена на действительность.
   *
   * @param token токен
   * @return true, если токен просрочен
   */
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Извлечение даты истечения токена.
   *
   * @param token токен
   * @return дата истечения
   */
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Извлечение всех данных из токена.
   *
   * @param token токен
   * @return данные
   */
  private Claims extractAllClaims(String token) {
    Claims claims;
    try {
      claims =
          Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
    } catch (ExpiredJwtException
        | UnsupportedJwtException
        | MalformedJwtException
        | IllegalArgumentException e) {
      log.error("Incorrect Jwt!", e);
      throw new JwtTokenException(
          ExceptionModel.builder()
              .exceptionEnum(ExceptionEnum.PARSE_JWT_TOKEN)
              .info(e.getMessage())
              .build());
    }

    return claims;
  }

  /**
   * Получение ключа для подписи токена.
   *
   * @return ключ
   */
  private Key getSigningKey() {
    var keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
