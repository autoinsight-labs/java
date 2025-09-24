package br.com.autoinsight.autoinsight_client.modules.roles;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {
  boolean existsByName(String name);

  boolean existsByAcronym(String acronym);

  Optional<RoleEntity> findByAcronym(String acronym);
}
