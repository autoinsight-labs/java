package br.com.autoinsight.autoinsight_client.modules.roles.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.roles.RoleEntity;
import br.com.autoinsight.autoinsight_client.modules.roles.dto.RoleDTO;
import br.com.autoinsight.autoinsight_client.modules.roles.mapper.RoleMapper;
import io.github.thibaultmeyer.cuid.CUID;

@Service
public class CreateRoleUseCase {

  @Autowired
  private RoleCachingUseCase roleCachingUseCase;

  public RoleDTO execute(RoleDTO dto) {
    if (roleCachingUseCase.existsByName(dto.getName())) {
      throw new RuntimeException("Role name already exists");
    }
    if (roleCachingUseCase.existsByAcronym(dto.getAcronym())) {
      throw new RuntimeException("Role acronym already exists");
    }

    RoleEntity entity = RoleMapper.toEntity(dto);
    entity.setId(CUID.randomCUID2(24).toString());

    RoleEntity saved = roleCachingUseCase.save(entity);
    roleCachingUseCase.clearCache();
    return RoleMapper.toDTO(saved);
  }
}
