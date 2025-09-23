package br.com.autoinsight.autoinsight_client.modules.roles.useCases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.roles.RoleEntity;
import br.com.autoinsight.autoinsight_client.modules.roles.RoleRepository;

@Service
public class RoleCachingUseCase {

  @Autowired
  private RoleRepository roleRepository;

  @Cacheable(value = "roles", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
  public Page<RoleEntity> findAll(Pageable pageable) {
    return roleRepository.findAll(pageable);
  }

  @Cacheable(value = "roles", key = "#id")
  public Optional<RoleEntity> findById(String id) {
    return roleRepository.findById(id);
  }

  public RoleEntity save(RoleEntity entity) {
    return roleRepository.save(entity);
  }

  public boolean existsByName(String name) {
    return roleRepository.existsByName(name);
  }

  public boolean existsByAcronym(String acronym) {
    return roleRepository.existsByAcronym(acronym);
  }

  @CacheEvict(value = "roles", allEntries = true)
  public void clearCache() {
  }

  public void deleteById(String id) {
    roleRepository.deleteById(id);
  }
}
