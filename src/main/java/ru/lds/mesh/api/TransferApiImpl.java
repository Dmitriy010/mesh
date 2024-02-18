package ru.lds.mesh.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.lds.mesh.servicies.TransferMoneyService;
import ru.lds.openapi.api.TransferApi;
import ru.lds.openapi.model.TransferMoneyRequest;

/** Реализация API для методов денежных переводов. */
@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferApiImpl implements TransferApi {

  TransferMoneyService transferMoneyService;

  @Override
  public ResponseEntity<Void> transferMoney(
      Long userId, TransferMoneyRequest transferMoneyRequest) {
    log.info(
        "Запрос на перевод денежных средств, userId: {}, value: {}",
        userId,
        transferMoneyRequest.getValue());

    transferMoneyService.transferMoneyToUser(userId, transferMoneyRequest);

    return ResponseEntity.accepted().build();
  }
}
