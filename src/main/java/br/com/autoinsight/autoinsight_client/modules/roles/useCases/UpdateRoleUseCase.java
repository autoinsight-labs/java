package br.com.autoinsight.autoinsight_client.modules.roles.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.roles.RoleEntity;
import br.com.autoinsight.autoinsight_client.modules.roles.dto.RoleDTO;
import br.com.autoinsight.autoinsight_client.modules.roles.mapper.RoleMapper;

@Service
public class UpdateRoleUseCase {

  @Autowired
  private RoleCachingUseCase roleCachingUseCase;

  public RoleDTO execute(String id, RoleDTO dto) {
    RoleEntity role = roleCachingUseCase.findById(id)
        .orElseThrow(() -> new RuntimeException("Role not found"));

    if (dto.getName() != null) {
      role.setName(dto.getName());
    }
    if (dto.getAcronym() != null) {
      role.setAcronym(dto.getAcronym());
    }

    RoleEntity saved = roleCachingUseCase.save(role);
    roleCachingUseCase.clearCache();
    return RoleMapper.toDTO(saved);
  }
}
