package ru.lds.mesh.servicies.impl;

import java.util.List;
import javax.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ru.lds.mesh.dtos.EmailDataDto;
import ru.lds.mesh.exceptions.ExceptionEnum;
import ru.lds.mesh.exceptions.ExceptionModel;
import ru.lds.mesh.exceptions.ExistsException;
import ru.lds.mesh.exceptions.NotFoundException;
import ru.lds.mesh.mappers.EmailDataMapper;
import ru.lds.mesh.repositories.EmailDataRepository;
import ru.lds.mesh.servicies.EmailDataService;
import ru.lds.openapi.model.EmailResponse;

/** Реализация сервиса, предоставляющего методы для работы с электронной почтой. */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailDataServiceImpl implements EmailDataService {

  EmailDataRepository emailDataRepository;

  EmailDataMapper emailDataMapper;

  @Override
  @Transactional
  @CacheEvict(value = "users", allEntries = true)
  public List<EmailResponse> addEmailByUserId(Long userId, String email) {
    if (emailDataRepository.existsByEmail(email)) {
      throw new ExistsException(
          ExceptionModel.builder().exceptionEnum(ExceptionEnum.EMAIL_EXISTS).build());
    }

    var emailDataEntity =
        emailDataMapper.emailDataDtoToEmailDataEntity(new EmailDataDto(userId, email));
    var savedEmailDataEntity = emailDataRepository.save(emailDataEntity);
    var emailDataEntityList =
        emailDataRepository.findAllByUserEntityId(savedEmailDataEntity.getUserEntity().getId());

    return emailDataMapper.emailDataEntityToEmailResponse(emailDataEntityList);
  }

  @Override
  @Transactional
  @CacheEvict(value = "users", allEntries = true)
  public List<EmailResponse> deleteEmailById(Long emailId) {
    var emailDataEntity =
        emailDataRepository
            .findById(emailId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ExceptionModel.builder()
                            .exceptionEnum(ExceptionEnum.EMAIL_NOT_FOUND)
                            .build()));

    emailDataRepository.deleteById(emailId);
    var emailDataEntityList =
        emailDataRepository.findAllByUserEntityId(emailDataEntity.getUserEntity().getId());

    return emailDataMapper.emailDataEntityToEmailResponse(emailDataEntityList);
  }

  @Override
  @Transactional
  @CacheEvict(value = "users", allEntries = true)
  public List<EmailResponse> updateEmailById(Long emailId, String email) {
    var existsEmailData =
        emailDataRepository
            .findById(emailId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ExceptionModel.builder()
                            .exceptionEnum(ExceptionEnum.EMAIL_NOT_FOUND)
                            .build()));

    existsEmailData.setEmail(email);
    var emailDataEntityList =
        emailDataRepository.findAllByUserEntityId(existsEmailData.getUserEntity().getId());

    return emailDataMapper.emailDataEntityToEmailResponse(emailDataEntityList);
  }
}
