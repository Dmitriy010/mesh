package ru.lds.mesh.exceptions.handler;

import javax.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.lds.mesh.exceptions.AccessException;
import ru.lds.mesh.exceptions.BadRequestException;
import ru.lds.mesh.exceptions.ConflictException;
import ru.lds.mesh.exceptions.ExceptionEnum;
import ru.lds.mesh.exceptions.ExceptionModel;
import ru.lds.mesh.exceptions.ExistsException;
import ru.lds.mesh.exceptions.JwtTokenException;
import ru.lds.mesh.exceptions.NotFoundException;
import ru.lds.mesh.mappers.ErrorMapper;
import ru.lds.openapi.model.ErrorResponse;

/** Класс, представляющий обработчик ошибок в приложении. */
@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorHandler {

  ErrorMapper errorMapper;

  /**
   * Обрабатывает исключение NotFoundException.
   *
   * @param ex Исключение NotFoundException.
   * @return Ответ с ошибкой и статусом NOT_FOUND.
   */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
    var errorResponse = errorMapper.exceptionToErrorResponse(ex);

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * Обрабатывает исключение ExistsException.
   *
   * @param ex Исключение ExistsException.
   * @return Ответ с ошибкой и статусом CONFLICT.
   */
  @ExceptionHandler(ExistsException.class)
  public ResponseEntity<ErrorResponse> handleExistsException(ExistsException ex) {
    var errorResponse = errorMapper.exceptionToErrorResponse(ex);

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  /**
   * Обрабатывает исключение AuthenticationException.
   *
   * @param ex Исключение AuthenticationException.
   * @return Ответ с ошибкой и статусом UNAUTHORIZED.
   */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
    var accessException =
        new AccessException(
            ExceptionModel.builder()
                .exceptionEnum(ExceptionEnum.AUTHENTICATION)
                .info(ex.getMessage())
                .build());

    var errorResponse = errorMapper.exceptionToErrorResponse(accessException);

    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  /**
   * Обрабатывает исключение JwtTokenException.
   *
   * @param ex Исключение JwtTokenException.
   * @return Ответ с ошибкой и статусом UNAUTHORIZED.
   */
  @ExceptionHandler(JwtTokenException.class)
  public ResponseEntity<ErrorResponse> handleJwtTokenException(JwtTokenException ex) {
    var errorResponse = errorMapper.exceptionToErrorResponse(ex);

    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  /**
   * Обрабатывает исключение AccessException.
   *
   * @param ex Исключение AccessException.
   * @return Ответ с ошибкой и статусом FORBIDDEN.
   */
  @ExceptionHandler(AccessException.class)
  public ResponseEntity<ErrorResponse> handleAccessException(AccessException ex) {
    var errorResponse = errorMapper.exceptionToErrorResponse(ex);

    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  /**
   * Обрабатывает исключение ConflictException.
   *
   * @param ex Исключение ConflictException.
   * @return Ответ с ошибкой и статусом CONFLICT.
   */
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
    var errorResponse = errorMapper.exceptionToErrorResponse(ex);

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  /**
   * Обрабатывает исключение ConstraintViolationException.
   *
   * @param ex Исключение ConstraintViolationException.
   * @return Ответ с ошибкой и статусом BAD_REQUEST.
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    var badRequestException =
        new BadRequestException(
            ExceptionModel.builder()
                .exceptionEnum(ExceptionEnum.VALIDATION)
                .info(ex.getMessage())
                .build());

    var errorResponse = errorMapper.exceptionToErrorResponse(badRequestException);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
