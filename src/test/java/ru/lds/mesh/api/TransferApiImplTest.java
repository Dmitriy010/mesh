package ru.lds.mesh.api;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import ru.lds.mesh.TestUtils;
import ru.lds.mesh.servicies.TransferMoneyService;
import ru.lds.openapi.api.TransferApi;
import ru.lds.openapi.model.TransferMoneyRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = TransferApiImpl.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class TransferApiImplTest {

  @Autowired TransferApi transferApi;

  @MockBean TransferMoneyService transferMoneyService;

  @Test
  @DisplayName("Перевод денежных средств пользователю")
  void whenTransferMoneyThenOk() {
    var userId = TestUtils.generateRandomObject(Long.class);
    var transferMoneyRequest = TestUtils.generateRandomObject(TransferMoneyRequest.class);

    var responseEntity = transferApi.transferMoney(userId, transferMoneyRequest);

    assertEquals(ResponseEntity.accepted().build(), responseEntity);

    verify(transferMoneyService).transferMoneyToUser(userId, transferMoneyRequest);
  }
}
