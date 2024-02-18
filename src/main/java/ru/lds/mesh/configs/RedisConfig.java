package ru.lds.mesh.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/** Конфигурация базы данных Redis. */
@Configuration
@EnableCaching
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String redisHost;

  @Value("${spring.redis.port}")
  private int redisPort;

  /**
   * Создает фабрику подключения JedisConnectionFactory.
   *
   * @return JedisConnectionFactory объект.
   */
  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    var standaloneConfig = new RedisStandaloneConfiguration(redisHost, redisPort);

    return new JedisConnectionFactory(standaloneConfig);
  }

  @Bean
  public RedisCacheManager cacheManager() {
    return RedisCacheManager.builder(jedisConnectionFactory()).build();
  }
}
