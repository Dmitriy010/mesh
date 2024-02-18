package ru.lds.mesh.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lds.mesh.entities.EmailDataEntity;

/** Репозиторий для работы с сущностью электронной почты. */
@Repository
public interface EmailDataRepository extends JpaRepository<EmailDataEntity, Long> {

  boolean existsByEmail(String email);

  List<EmailDataEntity> findAllByUserEntityId(Long userId);
}
