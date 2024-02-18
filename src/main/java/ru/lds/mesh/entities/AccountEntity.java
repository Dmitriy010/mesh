package ru.lds.mesh.entities;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Представляет сущность аккаунта пользователя. */
@Entity
@Table(name = "ACCOUNT")
@NoArgsConstructor
@Getter
@Setter
public class AccountEntity {

  /** Уникальный идентификатор аккаунта. */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq_generator")
  @SequenceGenerator(
      name = "account_seq_generator",
      sequenceName = "account_id_seq",
      allocationSize = 1)
  @Column(name = "ID")
  Long id;

  /** Связанная с аккаунтом сущность пользователя. */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false, unique = true)
  UserEntity userEntity;

  /** Начальный баланс аккаунта. */
  @Column(name = "BALANCE")
  @PositiveOrZero
  BigDecimal balance;

  /** Фактический баланс аккаунта. */
  @Column(name = "ACTUAL_BALANCE")
  @PositiveOrZero
  BigDecimal actualBalance;
}
