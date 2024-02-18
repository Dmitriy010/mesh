package ru.lds.mesh.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/** Представляет сущность данных о телефоне пользователя. */
@Entity
@Table(name = "PHONE_DATA")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneDataEntity {

  /** Уникальный идентификатор сущности. */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_data_seq_generator")
  @SequenceGenerator(
      name = "phone_data_seq_generator",
      sequenceName = "phone_data_id_seq",
      allocationSize = 1)
  @Column(name = "ID")
  Long id;

  /** Связанная сущность пользователя. */
  @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  UserEntity userEntity;

  /** Номер телефона. */
  @Column(name = "PHONE", length = 13)
  @Pattern(regexp = "7\\d{10}")
  String phone;
}
