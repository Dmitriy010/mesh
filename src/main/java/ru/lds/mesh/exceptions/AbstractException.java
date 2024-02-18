package ru.lds.mesh.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Абстрактный класс исключения, представляющий базовую реализацию для всех пользовательских
 * исключений.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class AbstractException extends RuntimeException {

  ExceptionModel exceptionModel;

  public AbstractException(ExceptionModel exceptionModel) {
    super(exceptionModel.getExceptionEnum().getMessage());
    this.exceptionModel = exceptionModel;
  }
}
