package br.com.autoinsight.autoinsight_client.modules.users.useCases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.users.UsersEntity;
import br.com.autoinsight.autoinsight_client.modules.users.UsersRepository;

@Service
public class UsersCachingUseCase {

  @Autowired
  private UsersRepository usersRepository;

  @Cacheable(value = "users", key = "#id")
  public Optional<UsersEntity> findById(String id) {
    return usersRepository.findById(id);
  }

  @Cacheable(value = "users", key = "'email_' + #email")
  public Optional<UsersEntity> findByEmail(String email) {
    return usersRepository.findByEmail(email);
  }

  @Cacheable(value = "users", key = "'page_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()")
  public Page<UsersEntity> findAll(Pageable pageable) {
    return usersRepository.findAll(pageable);
  }

  @Cacheable(value = "users", key = "'exists_' + #id")
  public boolean existsById(String id) {
    return usersRepository.existsById(id);
  }

  @Cacheable(value = "users", key = "'existsByEmail_' + #email")
  public boolean existsByEmail(String email) {
    return usersRepository.existsByEmail(email);
  }

  @CacheEvict(value = "users", allEntries = true)
  public UsersEntity save(UsersEntity user) {
    return usersRepository.save(user);
  }

  @CacheEvict(value = "users", allEntries = true)
  public void deleteById(String id) {
    usersRepository.deleteById(id);
  }

  @CacheEvict(value = "users", allEntries = true)
  public void clearCache() {
  }
}
