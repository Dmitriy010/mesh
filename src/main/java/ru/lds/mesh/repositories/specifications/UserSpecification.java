package ru.lds.mesh.repositories.specifications;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.persistence.criteria.JoinType;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.lds.mesh.entities.EmailDataEntity_;
import ru.lds.mesh.entities.PhoneDataEntity_;
import ru.lds.mesh.entities.UserEntity;
import ru.lds.mesh.entities.UserEntity_;
import ru.lds.openapi.model.UserFilterRequest;

/** Класс, предоставляющий спецификации для фильтрации пользователей. */
@UtilityClass
public class UserSpecification {

  /**
   * Возвращает спецификацию, которая проверяет, что дата рождения пользователя больше заданной
   * даты.
   *
   * @param dateOfBirth Дата рождения для сравнения
   * @return Спецификация для сравнения даты рождения
   */
  private Specification<UserEntity> greaterThanDateOfBirth(LocalDate dateOfBirth) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThan(root.get(UserEntity_.DATE_OF_BIRTH), dateOfBirth);
  }

  /**
   * Возвращает спецификацию, которая проверяет, что телефон пользователя равен заданному телефону.
   *
   * @param phone Телефон для сравнения
   * @return Спецификация для сравнения телефона
   */
  private Specification<UserEntity> equalPhone(String phone) {
    return (root, query, criteriaBuilder) -> {
      var phoneJoin = root.joinSet(UserEntity_.PHONE_DATA_ENTITIES, JoinType.INNER);

      return criteriaBuilder.equal(phoneJoin.get(PhoneDataEntity_.PHONE), phone);
    };
  }

  /**
   * Возвращает спецификацию, которая проверяет, что имя пользователя начинается с заданной
   * подстроки.
   *
   * @param name Подстрока для сравнения
   * @return Спецификация для сравнения имени
   */
  private Specification<UserEntity> likeName(String name) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(root.get(UserEntity_.NAME), name + "%");
  }

  /**
   * Возвращает спецификацию, которая проверяет, что почта пользователя равна заданной почте.
   *
   * @param email почта для сравнения
   * @return Спецификация для сравнения почты
   */
  private Specification<UserEntity> equalEmail(String email) {
    return (root, query, criteriaBuilder) -> {
      var emailJoin = root.joinSet(UserEntity_.EMAIL_DATA_ENTITIES, JoinType.INNER);

      return criteriaBuilder.equal(emailJoin.get(EmailDataEntity_.EMAIL), email);
    };
  }

  /**
   * Возвращает спецификацию, которая выполняет присоединение связанных сущностей.
   *
   * @return Спецификация для присоединения связанных сущностей
   */
  private Specification<UserEntity> joinAssociations() {
    return (root, query, criteriaBuilder) -> {
      root.join(UserEntity_.ACCOUNT_ENTITY, JoinType.INNER);
      return null;
    };
  }

  /**
   * Возвращает спецификацию, которая сортирует результаты по убыванию идентификатора.
   *
   * @return Спецификация для сортировки по убыванию идентификатора
   */
  private Specification<UserEntity> sortByIdDesc() {
    return (root, query, criteriaBuilder) -> {
      query.orderBy(criteriaBuilder.desc(root.get(UserEntity_.ID)));
      return null;
    };
  }

  /**
   * Возвращает спецификацию для фильтрации пользователей на основе фильтра пользователей.
   *
   * @param userFilter Фильтр пользователей
   * @return Спецификация для фильтрации пользователей
   */
  public static Specification<UserEntity> getByUserFilter(UserFilterRequest userFilter) {
    return Specification.where(joinAssociations())
        .and(StringUtils.isEmpty(userFilter.getEmail()) ? null : equalEmail(userFilter.getEmail()))
        .and(StringUtils.isEmpty(userFilter.getName()) ? null : likeName(userFilter.getName()))
        .and(StringUtils.isEmpty(userFilter.getPhone()) ? null : equalPhone(userFilter.getPhone()))
        .and(
            Objects.isNull(userFilter.getDateOfBirth())
                ? null
                : greaterThanDateOfBirth(
                    LocalDate.parse(
                        userFilter.getDateOfBirth(), DateTimeFormatter.ofPattern("dd.MM.yyyy"))))
        .and(sortByIdDesc());
  }
}
