package ru.lds.mesh;

import lombok.experimental.UtilityClass;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

/** Вспомогательный класс для тестирования. */
@UtilityClass
public class TestUtils {

  private static final EasyRandomParameters PARAMETERS =
      new EasyRandomParameters()
          .collectionSizeRange(1, 5)
          .stringLengthRange(10, 20)
          .scanClasspathForConcreteTypes(true);

  private static final EasyRandom EASY_RANDOM = new EasyRandom(PARAMETERS);

  /**
   * Генерирует случайный объект заданного класса.
   *
   * @param clazz класс объекта для генерации
   * @param <T> тип объекта
   * @return случайно сгенерированный объект заданного класса
   */
  public static <T> T generateRandomObject(Class<T> clazz) {
    return EASY_RANDOM.nextObject(clazz);
  }
}
