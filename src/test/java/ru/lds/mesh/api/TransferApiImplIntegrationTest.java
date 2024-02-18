package ru.lds.mesh.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.lds.mesh.SpringBootApplicationTest;
import ru.lds.mesh.exceptions.ExceptionEnum;
import ru.lds.mesh.security.JwtAuthenticationFilter;
import ru.lds.mesh.servicies.AuthenticationService;
import ru.lds.openapi.model.TransferMoneyRequest;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JwtAuthenticationFilter.class})
class TransferApiImplIntegrationTest extends SpringBootApplicationTest {

  private static final long USER_1_ID = 1L;
  private static final long USER_2_ID = 2L;
  private static final long USER_3_ID = 3L;
  private static final long NOT_EXISTS_USER = 4L;
  private static final BigDecimal TRANSFER_VALUE = new BigDecimal(10);
  private static final String DATE_TIME_PATTERN = "\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}";

  @Autowired MockMvc mockMvc;

  @MockBean AuthenticationService authenticationService;

  @Test
  @DisplayName("Перевод денежных средств от user2 к user3")
  void whenTransferMoneyThen200() throws Exception {
    var transferMoneyRequest = new TransferMoneyRequest(TRANSFER_VALUE);

    when(authenticationService.getCurrentUserIdFromJwtToken()).thenReturn(USER_2_ID);

    mockMvc
        .perform(
            post("/transfer/users/" + USER_3_ID)
                .content(new ObjectMapper().writeValueAsString(transferMoneyRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted());

    verify(authenticationService).getCurrentUserIdFromJwtToken();
  }

  @Test
  @DisplayName("Перевод денежных средств самому себе")
  void whenTransferMoneyToMyselfThen403() throws Exception {
    var transferMoneyRequest = new TransferMoneyRequest(TRANSFER_VALUE);

    when(authenticationService.getCurrentUserIdFromJwtToken()).thenReturn(USER_2_ID);

    mockMvc
        .perform(
            post("/transfer/users/" + USER_2_ID)
                .content(new ObjectMapper().writeValueAsString(transferMoneyRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.code").value(ExceptionEnum.TRANSFER_MONEY.getCode()))
        .andExpect(jsonPath("$.message").value(ExceptionEnum.TRANSFER_MONEY.getMessage()))
        .andExpect(jsonPath("$.info").value("Невозможно осуществить перевод самому себе"))
        .andExpect(jsonPath("$.time").exists())
        .andExpect(jsonPath("$.time", matchesPattern(DATE_TIME_PATTERN)));

    verify(authenticationService).getCurrentUserIdFromJwtToken();
  }

  @Test
  @DisplayName("Перевод денежных средств от user2 к не существующему пользователю")
  void whenTransferMoneyToNotExistsUserThen404() throws Exception {
    var transferMoneyRequest = new TransferMoneyRequest(TRANSFER_VALUE);

    when(authenticationService.getCurrentUserIdFromJwtToken()).thenReturn(USER_2_ID);

    mockMvc
        .perform(
            post("/transfer/users/" + NOT_EXISTS_USER)
                .content(new ObjectMapper().writeValueAsString(transferMoneyRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ExceptionEnum.ACCOUNT_NOT_FOUND.getCode()))
        .andExpect(jsonPath("$.message").value(ExceptionEnum.ACCOUNT_NOT_FOUND.getMessage()))
        .andExpect(jsonPath("$.info").doesNotExist())
        .andExpect(jsonPath("$.time").exists())
        .andExpect(jsonPath("$.time", matchesPattern(DATE_TIME_PATTERN)));

    verify(authenticationService).getCurrentUserIdFromJwtToken();
  }

  @Test
  @DisplayName(
      "Перевод денежных средств от user1(c недостаточным актуальным балансом для перевода) к user2")
  void whenTransferMoneyToNotEnoughActualBalanceUserThen409() throws Exception {
    var transferMoneyRequest = new TransferMoneyRequest(TRANSFER_VALUE);

    when(authenticationService.getCurrentUserIdFromJwtToken()).thenReturn(USER_1_ID);

    mockMvc
        .perform(
            post("/transfer/users/" + USER_2_ID)
                .content(new ObjectMapper().writeValueAsString(transferMoneyRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(
            jsonPath("$.code").value(ExceptionEnum.ACCOUNT_ACTUAL_BALANCE_NOT_ENOUGH.getCode()))
        .andExpect(
            jsonPath("$.message")
                .value(ExceptionEnum.ACCOUNT_ACTUAL_BALANCE_NOT_ENOUGH.getMessage()))
        .andExpect(jsonPath("$.info").doesNotExist())
        .andExpect(jsonPath("$.time").exists())
        .andExpect(jsonPath("$.time", matchesPattern(DATE_TIME_PATTERN)));

    verify(authenticationService).getCurrentUserIdFromJwtToken();
  }
}
