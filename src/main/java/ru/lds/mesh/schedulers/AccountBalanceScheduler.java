package ru.lds.mesh.schedulers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.lds.mesh.servicies.AccountService;

/** Класс, предоставляющий планировщик задач для увеличения балансов пользователей. */
@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountBalanceScheduler {

  AccountService accountService;

  /** Метод, вызываемый планировщиком задач, для увеличения балансов пользователей. */
  @Scheduled(fixedRateString = "${scheduler.intervalIncreaseBalance}")
  public void increaseAccountBalances() {
    log.info("Планировщик задач начал увеличение балансов пользователей");
    accountService.findAllForIncreaseActualBalance().forEach(accountService::increaseActualBalance);
    log.info("Планировщик задач закончил увеличение балансов пользователей");
  }
}
