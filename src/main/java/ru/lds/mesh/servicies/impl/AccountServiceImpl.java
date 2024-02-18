package ru.lds.mesh.servicies.impl;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import ru.lds.mesh.entities.AccountEntity;
import ru.lds.mesh.exceptions.ConflictException;
import ru.lds.mesh.exceptions.ExceptionEnum;
import ru.lds.mesh.exceptions.ExceptionModel;
import ru.lds.mesh.exceptions.NotFoundException;
import ru.lds.mesh.repositories.AccountRepository;
import ru.lds.mesh.servicies.AccountService;

/** Реализация сервиса, предоставляющего методы для работы с балансом аккаунта. */
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  @Value("${scheduler.maxIncreaseMultiplier}")
  private double maxIncreaseMultiplier;

  @Value("${scheduler.increaseMultiplier}")
  private double increaseMultiplier;

  private final AccountRepository accountRepository;

  @Override
  public List<AccountEntity> findAllForIncreaseActualBalance() {
    return accountRepository.findAllByActualBalanceLessThanBalance(
        BigDecimal.valueOf(maxIncreaseMultiplier));
  }

  @Override
  @Transactional
  @CacheEvict(value = "users", allEntries = true)
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public void increaseActualBalance(AccountEntity accountEntity) {
    log.debug(
        "Увеличение актуального баланса аккаунта, accountId: {}, actualBalance: {}",
        accountEntity.getId(),
        accountEntity.getActualBalance());

    var actualBalance = accountEntity.getActualBalance();
    var increaseAmount = actualBalance.multiply(BigDecimal.valueOf(increaseMultiplier));
    var newActualBalance = actualBalance.add(increaseAmount);

    accountEntity.setActualBalance(newActualBalance);
    accountRepository.save(accountEntity);

    log.debug(
        "Актуальный баланс аккаунта изменен, "
            + "accountId: {}, increaseAmount: {}, oldActualBalance: {}, newActualBalance: {}",
        accountEntity.getId(),
        increaseAmount,
        actualBalance,
        newActualBalance);
  }

  @Override
  @Transactional
  @CacheEvict(value = "users", allEntries = true)
  public void updateActualBalanceByUserId(Long userId, BigDecimal value) {
    log.debug("Обновление актуального баланса аккаунта, userId: {}, value: {}", userId, value);

    var accountEntity =
        accountRepository
            .findByUserEntityId(userId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ExceptionModel.builder()
                            .exceptionEnum(ExceptionEnum.ACCOUNT_NOT_FOUND)
                            .build()));

    var updatedBalance = accountEntity.getActualBalance().add(value);
    if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
      throw new ConflictException(
          ExceptionModel.builder()
              .exceptionEnum(ExceptionEnum.ACCOUNT_ACTUAL_BALANCE_NOT_ENOUGH)
              .build());
    }

    accountEntity.setActualBalance(updatedBalance);

    log.debug(
        "Обновлен актуальный баланс аккаунта, "
            + "userId: {}, oldActualBalance: {}, newActualBalance: {}",
        userId,
        accountEntity.getActualBalance(),
        updatedBalance);
  }
}
