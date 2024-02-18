package ru.lds.mesh.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lds.mesh.entities.AccountEntity;

/** Репозиторий для работы с аккаунтом пользователя. */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

  @Query("SELECT c FROM AccountEntity c WHERE c.actualBalance <= c.balance * :multiplier")
  List<AccountEntity> findAllByActualBalanceLessThanBalance(BigDecimal multiplier);

  Optional<AccountEntity> findByUserEntityId(Long userId);
}
