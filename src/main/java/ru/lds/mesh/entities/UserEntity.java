package ru.lds.mesh.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/** Представляет сущность пользователя. */
@Entity
@Table(name = "USERS")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {

  /** Уникальный идентификатор пользователя. */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_generator")
  @SequenceGenerator(
      name = "users_seq_generator",
      sequenceName = "users_id_seq",
      allocationSize = 1)
  @Column(name = "ID")
  Long id;

  /** Имя пользователя. */
  @Column(name = "NAME", length = 500)
  String name;

  /** Дата рождения пользователя. */
  @Column(name = "DATE_OF_BIRTH")
  LocalDate dateOfBirth;

  /** Пароль пользователя. */
  @Column(name = "PASSWORD", length = 500)
  @Size(min = 8, max = 500)
  String password;

  /** Набор сущностей телефонных данных, связанных с пользователем. */
  @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  Set<PhoneDataEntity> phoneDataEntities = new HashSet<>();

  /** Набор сущностей электронных данных почт, связанных с пользователем. */
  @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  Set<EmailDataEntity> emailDataEntities = new HashSet<>();

  /** Сущность аккаунта, связанная с пользователем. */
  @OneToOne(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
  AccountEntity accountEntity;
}
