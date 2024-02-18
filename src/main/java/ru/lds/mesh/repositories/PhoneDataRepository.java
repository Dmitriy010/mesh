package ru.lds.mesh.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lds.mesh.entities.PhoneDataEntity;

/** Репозиторий для работы с сущностью телефона. */
@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneDataEntity, Long> {

  boolean existsByPhone(String phone);

  List<PhoneDataEntity> findAllByUserEntityId(Long userId);
}
