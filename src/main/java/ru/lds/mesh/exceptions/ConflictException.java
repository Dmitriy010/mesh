package ru.lds.mesh.exceptions;

/** Исключение, которое выбрасывается в случае конфликта данных. */
public class ConflictException extends AbstractException {

  public ConflictException(ExceptionModel exceptionModel) {
    super(exceptionModel);
  }
}
