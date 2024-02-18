package ru.lds.mesh.servicies.impl;

import java.util.List;
import javax.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ru.lds.mesh.dtos.PhoneDataDto;
import ru.lds.mesh.exceptions.ConflictException;
import ru.lds.mesh.exceptions.ExceptionEnum;
import ru.lds.mesh.exceptions.ExceptionModel;
import ru.lds.mesh.exceptions.NotFoundException;
import ru.lds.mesh.mappers.PhoneDataMapper;
import ru.lds.mesh.repositories.PhoneDataRepository;
import ru.lds.mesh.servicies.PhoneDataService;
import ru.lds.openapi.model.PhoneResponse;

/** Реализация сервиса, предоставляющего методы для работы с данными о телефонах. */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PhoneDataServiceImpl implements PhoneDataService {

  PhoneDataRepository phoneDataRepository;

  PhoneDataMapper phoneDataMapper;

  @Override
  @Transactional
  @CacheEvict(value = "users", allEntries = true)
  public List<PhoneResponse> addPhoneByUserId(Long userId, String number) {
    if (phoneDataRepository.existsByPhone(number)) {
      throw new ConflictException(
          ExceptionModel.builder().exceptionEnum(ExceptionEnum.PHONE_EXISTS).build());
    }

    var phoneDataEntity =
        phoneDataMapper.phoneDataDtoToPhoneDataEntity(new PhoneDataDto(userId, number));
    var savedPhoneDataEntity = phoneDataRepository.save(phoneDataEntity);
    var phoneDataEntityList =
        phoneDataRepository.findAllByUserEntityId(savedPhoneDataEntity.getUserEntity().getId());

    return phoneDataMapper.phoneDataEntityToPhoneResponse(phoneDataEntityList);
  }

  @Override
  @Transactional
  @CacheEvict(value = "users", allEntries = true)
  public List<PhoneResponse> deletePhoneById(Long phoneId) {
    var phoneDataEntity =
        phoneDataRepository
            .findById(phoneId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ExceptionModel.builder()
                            .exceptionEnum(ExceptionEnum.PHONE_NOT_FOUND)
                            .build()));

    phoneDataRepository.deleteById(phoneId);
    var phoneDataEntityList =
        phoneDataRepository.findAllByUserEntityId(phoneDataEntity.getUserEntity().getId());

    return phoneDataMapper.phoneDataEntityToPhoneResponse(phoneDataEntityList);
  }

  @Override
  @Transactional
  @CacheEvict(value = "users", allEntries = true)
  public List<PhoneResponse> updatePhoneById(Long phoneId, String number) {
    var existsPhoneData =
        phoneDataRepository
            .findById(phoneId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ExceptionModel.builder()
                            .exceptionEnum(ExceptionEnum.PHONE_NOT_FOUND)
                            .build()));

    existsPhoneData.setPhone(number);
    var phoneDataEntityList =
        phoneDataRepository.findAllByUserEntityId(existsPhoneData.getUserEntity().getId());

    return phoneDataMapper.phoneDataEntityToPhoneResponse(phoneDataEntityList);
  }
}
