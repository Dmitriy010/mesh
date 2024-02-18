package ru.lds.mesh.exceptions;

/** Исключение, которое выбрасывается в случае, когда запрашиваемый ресурс не найден. */
public class NotFoundException extends AbstractException {

  public NotFoundException(ExceptionModel exceptionModel) {
    super(exceptionModel);
  }
}
