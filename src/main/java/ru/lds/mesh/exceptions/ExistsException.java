package ru.lds.mesh.exceptions;

/** Исключение, которое выбрасывается в случае, когда запрашиваемая сущность уже существует. */
public class ExistsException extends AbstractException {

  public ExistsException(ExceptionModel exceptionModel) {
    super(exceptionModel);
  }
}
