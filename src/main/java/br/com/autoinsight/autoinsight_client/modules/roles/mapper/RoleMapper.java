package br.com.autoinsight.autoinsight_client.modules.roles.mapper;

import br.com.autoinsight.autoinsight_client.modules.roles.RoleEntity;
import br.com.autoinsight.autoinsight_client.modules.roles.dto.RoleDTO;

public class RoleMapper {
  public static RoleDTO toDTO(RoleEntity entity) {
    RoleDTO dto = new RoleDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setAcronym(entity.getAcronym());
    return dto;
  }

  public static RoleEntity toEntity(RoleDTO dto) {
    RoleEntity entity = new RoleEntity();
    entity.setId(dto.getId());
    entity.setName(dto.getName());
    entity.setAcronym(dto.getAcronym());
    return entity;
  }
}
