package ru.lds.mesh.servicies.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.lds.mesh.TestUtils;
import ru.lds.mesh.exceptions.AccessException;
import ru.lds.mesh.exceptions.ExceptionEnum;
import ru.lds.mesh.servicies.AccountService;
import ru.lds.mesh.servicies.AuthenticationService;
import ru.lds.mesh.servicies.TransferMoneyService;
import ru.lds.openapi.model.TransferMoneyRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TransferMoneyServiceImpl.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class TransferMoneyServiceImplTest {

  @Autowired TransferMoneyService transferMoneyService;

  @MockBean AuthenticationService authenticationService;
  @MockBean AccountService accountService;

  @Captor ArgumentCaptor<Long> userIdCaptor;
  @Captor ArgumentCaptor<BigDecimal> valueCaptor;

  @Test
  @DisplayName("Перевод денежных средств другому пользователю")
  void whenTransferMoneyToUserThenOk() {
    var userId = TestUtils.generateRandomObject(Long.class);
    var currentUserId = TestUtils.generateRandomObject(Long.class);
    var transferMoneyRequest = TestUtils.generateRandomObject(TransferMoneyRequest.class);
    var positiveValue = transferMoneyRequest.getValue();
    var negativeValue = positiveValue.negate();

    when(authenticationService.getCurrentUserIdFromJwtToken()).thenReturn(currentUserId);

    transferMoneyService.transferMoneyToUser(userId, transferMoneyRequest);

    verify(authenticationService).getCurrentUserIdFromJwtToken();

    verify(accountService, times(2))
        .updateActualBalanceByUserId(userIdCaptor.capture(), valueCaptor.capture());

    var capturedUserIds = userIdCaptor.getAllValues();
    var capturedBalances = valueCaptor.getAllValues();

    assertEquals(currentUserId, capturedUserIds.get(0));
    assertEquals(userId, capturedUserIds.get(1));
    assertEquals(negativeValue, capturedBalances.get(0));
    assertEquals(positiveValue, capturedBalances.get(1));
  }

  @Test
  @DisplayName("Перевод денежных средств самому себе")
  void whenTransferMoneyToCurrentUserThenAccessException() {
    var userId = TestUtils.generateRandomObject(Long.class);
    var transferMoneyRequest = TestUtils.generateRandomObject(TransferMoneyRequest.class);

    when(authenticationService.getCurrentUserIdFromJwtToken()).thenReturn(userId);

    var accessException =
        assertThrows(
            AccessException.class,
            () -> transferMoneyService.transferMoneyToUser(userId, transferMoneyRequest));

    var expectedMessage = ExceptionEnum.TRANSFER_MONEY.getMessage();
    var actualMessage = accessException.getMessage();

    assertEquals(expectedMessage, actualMessage);

    verify(authenticationService).getCurrentUserIdFromJwtToken();

    verify(accountService, never()).updateActualBalanceByUserId(anyLong(), any());

    verify(accountService, never()).updateActualBalanceByUserId(anyLong(), any());
  }
}
