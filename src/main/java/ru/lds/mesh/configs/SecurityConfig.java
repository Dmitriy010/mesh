package ru.lds.mesh.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import ru.lds.mesh.security.JwtAuthenticationFilter;
import ru.lds.mesh.servicies.CustomUserDetailsService;

/** Конфигурация безопасности. */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {

  JwtAuthenticationFilter jwtAuthenticationFilter;
  CustomUserDetailsService customUserDetailsService;

  /**
   * Создает цепочку фильтров безопасности.
   *
   * <p>Он выполняет следующие операции: - Отключает CSRF-защиту и настраивает CORS-политику для
   * разрешения запросов от разных источников. - Устанавливает правила доступа к различным
   * URL-адресам с помощью `authorizeHttpRequests`. - Устанавливает уровень безопасности сессии на
   * STATELESS, чтобы не использовать сессии на сервере. - Добавляет фильтр аутентификации JWT перед
   * фильтром `UsernamePasswordAuthenticationFilter`.
   *
   * @param http HttpSecurity объект для настройки цепочки фильтров безопасности.
   * @return Цепочка фильтров безопасности.
   * @throws Exception Если произошла ошибка во время настройки цепочки фильтров безопасности.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .cors(
            cors ->
                cors.configurationSource(
                    request -> {
                      var corsConfiguration = new CorsConfiguration();
                      corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                      corsConfiguration.setAllowedMethods(
                          List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                      corsConfiguration.setAllowedHeaders(List.of("*"));
                      corsConfiguration.setAllowCredentials(true);
                      return corsConfiguration;
                    }))
        .authorizeHttpRequests(
            request ->
                request
                    .mvcMatchers("/auth/**")
                    .permitAll()
                    .mvcMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**")
                    .permitAll()
                    .mvcMatchers("/actuator/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Возвращает AuthenticationProvider для аутентификации пользователей.
   *
   * @return AuthenticationProvider объект.
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(customUserDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  OpenAPI apiInfo() {
    return new OpenAPI()
        .info(new Info().title("Mesh API").description("Тестовое API").version("1.0.0"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "BearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
  }
}
