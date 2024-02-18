package ru.lds.mesh.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

/** Вспомогательный класс для работы с пагинацией. */
@UtilityClass
public class PageableUtils {

  private static final int ONE_PAGE = 1;

  /**
   * Создает объект PageRequest на основе номера страницы и количества записей.
   *
   * @param pageNumber Номер страницы
   * @param recordsNumber Количество записей на странице
   * @return Объект постраничного запроса
   */
  public static PageRequest createPageRequest(int pageNumber, int recordsNumber) {
    var offset = (pageNumber - ONE_PAGE) * recordsNumber;

    return PageRequest.of(pageNumber - ONE_PAGE, recordsNumber).withPage(offset / recordsNumber);
  }
}
