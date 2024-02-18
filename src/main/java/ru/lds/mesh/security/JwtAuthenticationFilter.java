package ru.lds.mesh.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.lds.mesh.servicies.impl.JwtServiceImpl;

/** Фильтр для аутентификации пользователей по JWT-токену. */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String HEADER_NAME = "Authorization";

  JwtServiceImpl jwtService;
  UserDetailsService userDetailsService;

  /**
   * Метод для обработки запроса и проверки аутентификации пользователя по JWT-токену.
   *
   * @param request объект для получения заголовка авторизации.
   * @param response объект для передачи запроса дальше по цепочке фильтров.
   * @param filterChain объект для передачи запроса дальше по цепочке фильтров.
   * @throws ServletException если происходит ошибка при обработке запроса.
   * @throws IOException если происходит ошибка при чтении или записи данных.
   */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    var authHeader = request.getHeader(HEADER_NAME);

    if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    var jwtToken = authHeader.substring(BEARER_PREFIX.length());
    var login = jwtService.extractLogin(jwtToken);

    if (StringUtils.isNotEmpty(login)
        && SecurityContextHolder.getContext().getAuthentication() == null) {
      var userDetails = userDetailsService.loadUserByUsername(login);

      if (jwtService.isTokenValid(jwtToken, userDetails)) {
        var context = SecurityContextHolder.createEmptyContext();

        var authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), null);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
      }
    }

    filterChain.doFilter(request, response);
  }
}
