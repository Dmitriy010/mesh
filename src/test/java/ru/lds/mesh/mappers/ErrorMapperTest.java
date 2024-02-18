package ru.lds.mesh.mappers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lds.mesh.TestUtils;
import ru.lds.mesh.exceptions.ExceptionModel;
import ru.lds.mesh.exceptions.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ErrorMapperImpl.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ErrorMapperTest {

  @Autowired ErrorMapper errorMapper;

  @Test
  @DisplayName("Маппинг исключения на ответное сообщение об ошибке")
  void whenExceptionToErrorResponseThenOk() {
    var notFoundException =
        new NotFoundException(TestUtils.generateRandomObject(ExceptionModel.class));

    var errorResponse = errorMapper.exceptionToErrorResponse(notFoundException);

    assertEquals(
        notFoundException.getExceptionModel().getExceptionEnum().getMessage(),
        errorResponse.getMessage());
    assertEquals(
        notFoundException.getExceptionModel().getExceptionEnum().getCode(),
        errorResponse.getCode());
    assertEquals(notFoundException.getExceptionModel().getInfo(), errorResponse.getInfo());
    assertEquals(notFoundException.getExceptionModel().getTime(), errorResponse.getTime());
  }
}
