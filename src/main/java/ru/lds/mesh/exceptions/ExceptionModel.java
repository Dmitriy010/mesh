package ru.lds.mesh.exceptions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/** Модель исключения, содержащая информацию об ошибке. */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionModel {

  ExceptionEnum exceptionEnum;

  @Builder.Default
  String time =
      LocalDateTime.now(ZoneId.of("Europe/Moscow"))
          .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

  String info;
}
