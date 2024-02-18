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
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Представляет сущность данных об электронной почте пользователя. */
@Entity
@Table(name = "EMAIL_DATA")
@NoArgsConstructor
@Getter
@Setter
public class EmailDataEntity {

  /** Уникальный идентификатор сущности. */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_data_seq_generator")
  @SequenceGenerator(
      name = "email_data_seq_generator",
      sequenceName = "email_data_id_seq",
      allocationSize = 1)
  @Column(name = "ID")
  Long id;

  /** Связанная сущность пользователя. */
  @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  UserEntity userEntity;

  /** Адрес электронной почты. */
  @Column(name = "EMAIL", length = 200, unique = true)
  @Email
  String email;
}
