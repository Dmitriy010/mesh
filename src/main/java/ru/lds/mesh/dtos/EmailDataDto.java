package ru.lds.mesh.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** DTO данных об электронной почте. */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDataDto {

  /** Идентификатор пользователя. */
  Long userId;

  /** Электронная почта. */
  String email;
}
