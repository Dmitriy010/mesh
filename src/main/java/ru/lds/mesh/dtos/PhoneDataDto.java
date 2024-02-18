package ru.lds.mesh.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** DTO данных о телефоне. */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneDataDto {

  /** Идентификатор пользователя. */
  Long userId;

  /** Номер телефона. */
  String phone;
}
