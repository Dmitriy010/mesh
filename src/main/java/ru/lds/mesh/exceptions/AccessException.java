package ru.lds.mesh.exceptions;

/** Исключение, которое выбрасывается в случае отсутствия доступа к ресурсу или операции. */
public class AccessException extends AbstractException {

  public AccessException(ExceptionModel exceptionModel) {
    super(exceptionModel);
  }
}
