package ru.lds.mesh.servicies;

import java.math.BigDecimal;
import java.util.List;
import ru.lds.mesh.entities.AccountEntity;

/** Сервис, предоставляющий методы для работы с балансом аккаунта. */
public interface AccountService {

  /** Находит все аккаунты у которых может быть увеличен актуальный баланс. */
  List<AccountEntity> findAllForIncreaseActualBalance();

  /**
   * Увеличивает актуальный баланс аккаунта.
   *
   * @param accountEntity Аккаунт, баланс которого будет увеличен
   */
  void increaseActualBalance(AccountEntity accountEntity);

  /**
   * Обновляет актуальный баланс пользователя по указанному идентификатору.
   *
   * @param userId Идентификатор пользователя
   * @param value Значение, на которое нужно обновить баланс
   */
  void updateActualBalanceByUserId(Long userId, BigDecimal value);
}
