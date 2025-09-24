package br.com.autoinsight.autoinsight_client.modules.users.mapper;

import org.springframework.stereotype.Component;

import br.com.autoinsight.autoinsight_client.modules.users.UsersEntity;
import br.com.autoinsight.autoinsight_client.modules.users.dto.UserResponseDTO;

@Component
public class UsersMapper {

  public UserResponseDTO toResponseDTO(UsersEntity entity) {
    UserResponseDTO dto = new UserResponseDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setEmail(entity.getEmail());
    if (entity.getRole() != null) {
      dto.setRoleId(entity.getRole().getId());
    }
    return dto;
  }
}