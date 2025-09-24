package br.com.autoinsight.autoinsight_client.modules.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity, String> {
  Optional<UsersEntity> findByEmail(String email);
  boolean existsByEmail(String email);
} 