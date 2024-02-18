package ru.lds.mesh.servicies;

import ru.lds.openapi.model.TransferMoneyRequest;

/** Сервис, предоставляющий методы для денежных переводов. */
public interface TransferMoneyService {

  /**
   * Перевод денежных средств пользователю.
   *
   * @param userId идентификатор пользователя, которому нужно перевести деньги
   * @param transferMoneyRequest объект с информацией о переводе денег
   */
  void transferMoneyToUser(Long userId, TransferMoneyRequest transferMoneyRequest);
}
