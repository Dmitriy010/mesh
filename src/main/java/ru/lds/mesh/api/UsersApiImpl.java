package ru.lds.mesh.api;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.lds.mesh.aspects.annotations.CheckUserByEmailId;
import ru.lds.mesh.aspects.annotations.CheckUserById;
import ru.lds.mesh.aspects.annotations.CheckUserByPhoneId;
import ru.lds.mesh.servicies.EmailDataService;
import ru.lds.mesh.servicies.PhoneDataService;
import ru.lds.mesh.servicies.UserService;
import ru.lds.openapi.api.UsersApi;
import ru.lds.openapi.model.EmailResponse;
import ru.lds.openapi.model.PhoneResponse;
import ru.lds.openapi.model.UserFilterRequest;
import ru.lds.openapi.model.UserPageResponse;

/** Реализация API для методов пользователя. */
@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UsersApiImpl implements UsersApi {

  UserService userService;
  PhoneDataService phoneDataService;
  EmailDataService emailDataService;

  @Override
  @CheckUserById
  public ResponseEntity<List<PhoneResponse>> addPhoneByUserId(Long userId, String number) {
    log.info("Запрос на добавление номера телефона, userId: {}, login: {}", userId, number);
    return ResponseEntity.ok(phoneDataService.addPhoneByUserId(userId, number));
  }

  @Override
  @CheckUserByPhoneId
  public ResponseEntity<List<PhoneResponse>> deletePhoneById(Long phoneId) {
    log.info("Запрос на удаление номера телефона, phoneId: {}", phoneId);
    return ResponseEntity.ok(phoneDataService.deletePhoneById(phoneId));
  }

  @Override
  @CheckUserByPhoneId
  public ResponseEntity<List<PhoneResponse>> updatePhoneById(Long phoneId, String number) {
    log.info("Запрос на обновление номера телефона, phoneId: {}, number: {}", phoneId, number);
    return ResponseEntity.ok(phoneDataService.updatePhoneById(phoneId, number));
  }

  @Override
  @CheckUserById
  public ResponseEntity<List<EmailResponse>> addEmailByUserId(Long userId, String email) {
    log.info("Запрос на добавление электронной почты, userId: {}, email: {}", userId, email);
    return ResponseEntity.ok(emailDataService.addEmailByUserId(userId, email));
  }

  @Override
  @CheckUserByEmailId
  public ResponseEntity<List<EmailResponse>> deleteEmailById(Long emailId) {
    log.info("Запрос на удаление электронной почты, emailId: {}", emailId);
    return ResponseEntity.ok(emailDataService.deleteEmailById(emailId));
  }

  @Override
  @CheckUserByEmailId
  public ResponseEntity<List<EmailResponse>> updateEmailById(Long emailId, String email) {
    log.info("Запрос на обновление электронной почты', phoneId: {}, email: {}", emailId, email);
    return ResponseEntity.ok(emailDataService.updateEmailById(emailId, email));
  }

  @Override
  public ResponseEntity<UserPageResponse> findAllByUserFilterPage(
      UserFilterRequest userFilter, Integer pageNumber, Integer recordsNumber) {
    log.info(
        "Запрос на получение пользователей постранично, "
            + "dateOfBirth: {}, phone: {}, name: {}, email: {}, pageNumber: {}, recordsNumber: {}",
        userFilter.getDateOfBirth(),
        userFilter.getPhone(),
        userFilter.getName(),
        userFilter.getEmail(),
        pageNumber,
        recordsNumber);
    return ResponseEntity.ok(
        userService.findAllByUserFilterPage(userFilter, pageNumber, recordsNumber));
  }
}
