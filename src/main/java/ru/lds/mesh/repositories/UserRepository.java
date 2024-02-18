package ru.lds.mesh.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.lds.mesh.entities.UserEntity;

/** Репозиторий для работы с сущностью пользователя. */
@Repository
public interface UserRepository
    extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

  @NonNull
  @EntityGraph(attributePaths = {"phoneDataEntities", "emailDataEntities", "accountEntity"})
  Page<UserEntity> findAll(Specification<UserEntity> specification, @NonNull Pageable pageable);

  @EntityGraph(attributePaths = {"phoneDataEntities", "emailDataEntities"})
  Optional<UserEntity> findByEmailDataEntitiesEmailOrPhoneDataEntitiesPhone(
      String email, String phone);

  @EntityGraph(attributePaths = "emailDataEntities")
  Optional<UserEntity> findByEmailDataEntitiesId(Long emailId);

  @EntityGraph(attributePaths = {"phoneDataEntities"})
  Optional<UserEntity> findByPhoneDataEntitiesId(Long phoneId);
}
