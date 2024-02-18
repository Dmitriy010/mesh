package ru.lds.mesh.aspects;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.lds.mesh.aspects.annotations.CheckUserByEmailId;
import ru.lds.mesh.aspects.annotations.CheckUserById;
import ru.lds.mesh.aspects.annotations.CheckUserByPhoneId;
import ru.lds.mesh.exceptions.AccessException;
import ru.lds.mesh.exceptions.ExceptionEnum;
import ru.lds.mesh.exceptions.ExceptionModel;
import ru.lds.mesh.servicies.UserService;

/** Аспект для проверки доступа пользователя. */
@Aspect
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCheckAspect {

  UserService userService;

  /**
   * Проверяет доступ пользователя по идентификатору пользователя.
   *
   * @param joinPoint точка присоединения
   * @param checkUserById аннотация для проверки доступа по идентификатору пользователя
   */
  @Before("@annotation(checkUserById)")
  public void checkUserById(JoinPoint joinPoint, CheckUserById checkUserById) {
    var userId = (Long) joinPoint.getArgs()[0];
    checkMatchRequestUserAndCurrentUser(userService.findUserById(userId).getId());
  }

  /**
   * Проверяет доступ пользователя по идентификатору номера телефона.
   *
   * @param joinPoint точка присоединения
   * @param checkUserByPhoneId аннотация для проверки доступа по идентификатору номера телефона
   */
  @Before("@annotation(checkUserByPhoneId)")
  public void checkUserByPhoneId(JoinPoint joinPoint, CheckUserByPhoneId checkUserByPhoneId) {
    var phoneId = (Long) joinPoint.getArgs()[0];
    checkMatchRequestUserAndCurrentUser(userService.findUserByPhoneId(phoneId).getId());
  }

  /**
   * Проверяет доступ пользователя по идентификатору электронной почты.
   *
   * @param joinPoint точка присоединения
   * @param checkUserByEmailId аннотация для проверки доступа по идентификатору электронной почты
   */
  @Before("@annotation(checkUserByEmailId)")
  public void checkMatchRequestUserAndCurrentUser(
      JoinPoint joinPoint, CheckUserByEmailId checkUserByEmailId) {
    var emailId = (Long) joinPoint.getArgs()[0];
    checkMatchRequestUserAndCurrentUser(userService.findUserByEmailId(emailId).getId());
  }

  /**
   * Проверяет совпадение пользователя из запроса с текущим пользователем.
   *
   * @param requestUserId идентификатор пользователя, для которого проверяется доступ
   */
  private void checkMatchRequestUserAndCurrentUser(Long requestUserId) {
    var currentUserId = userService.getCurrentUser().getId();

    if (!currentUserId.equals(requestUserId)) {
      throw new AccessException(
          ExceptionModel.builder()
              .exceptionEnum(ExceptionEnum.FORBIDDEN)
              .info("Невозможно осуществить операцию для другого пользователя")
              .build());
    }
  }
}
