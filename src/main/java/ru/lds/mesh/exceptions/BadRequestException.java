package ru.lds.mesh.exceptions;

/** Исключение, которое выбрасывается в случае некорректного запроса. */
public class BadRequestException extends AbstractException {

  public BadRequestException(ExceptionModel exceptionModel) {
    super(exceptionModel);
  }
}
