package ru.lds.mesh.exceptions;

/** Исключение, которое выбрасывается в случае проблемы с JWT-токеном. */
public class JwtTokenException extends AbstractException {

  public JwtTokenException(ExceptionModel exceptionModel) {
    super(exceptionModel);
  }
}
