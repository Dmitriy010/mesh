package ru.lds.mesh.servicies.impl;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import ru.lds.mesh.exceptions.AccessException;
import ru.lds.mesh.exceptions.ExceptionEnum;
import ru.lds.mesh.exceptions.ExceptionModel;
import ru.lds.mesh.servicies.AccountService;
import ru.lds.mesh.servicies.AuthenticationService;
import ru.lds.mesh.servicies.TransferMoneyService;
import ru.lds.openapi.model.TransferMoneyRequest;

/** Реализация сервиса, предоставляющего методы для денежных переводов. */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferMoneyServiceImpl implements TransferMoneyService {

  AuthenticationService authenticationService;
  AccountService accountService;

  @Override
  @Transactional
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public void transferMoneyToUser(Long userId, TransferMoneyRequest transferMoneyRequest) {
    var currentUserId = authenticationService.getCurrentUserIdFromJwtToken();
    if (currentUserId.equals(userId)) {
      throw new AccessException(
          ExceptionModel.builder()
              .exceptionEnum(ExceptionEnum.TRANSFER_MONEY)
              .info("Невозможно осуществить перевод самому себе")
              .build());
    }

    accountService.updateActualBalanceByUserId(
        currentUserId, transferMoneyRequest.getValue().negate());
    accountService.updateActualBalanceByUserId(userId, transferMoneyRequest.getValue());
  }
}
