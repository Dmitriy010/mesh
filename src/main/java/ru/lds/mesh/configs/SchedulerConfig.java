package ru.lds.mesh.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/** Конфигурация планировщика задач. */
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

  @Value("${scheduler.poolSize}")
  private int poolSize;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    var taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(poolSize);
    taskScheduler.initialize();

    taskRegistrar.setTaskScheduler(taskScheduler);
  }
}
