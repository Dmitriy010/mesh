package ru.lds.mesh;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/** Запуск приложения с тест контейнером БД. */
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class SpringBootApplicationTest {

  private static final String DATABASE_NAME = "mesh-test-app";
  private static final String IMAGE_POSTGRES = "postgres:15";
  private static final String IMAGE_REDIS = "redis:6.2-alpine";
  private static final int REDIS_PORT = 6379;


  @Container
  static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(IMAGE_POSTGRES).withReuse(true).withDatabaseName(DATABASE_NAME);

  @Container
  static GenericContainer<?> redisContainer =
      new GenericContainer<>(IMAGE_REDIS).withExposedPorts(REDIS_PORT);

  @DynamicPropertySource
  static void datasourceProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
  }

  @DynamicPropertySource
  private static void registerRedisProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.redis.host", redisContainer::getHost);
    registry.add("spring.redis.port", () -> redisContainer.getMappedPort(REDIS_PORT).toString());
  }
}
