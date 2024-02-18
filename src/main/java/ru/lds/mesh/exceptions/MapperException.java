package ru.lds.mesh.exceptions;

/** Представляет сущность ошибки для MapStruct. */
public class MapperException extends RuntimeException {

  public MapperException() {
    super();
  }

  /**
   * Необходимый конструктор для MapStruct.
   *
   * @param message проблема маппинга
   */
  public MapperException(String message) {
    super(message);
  }
}
