package br.com.autoinsight.autoinsight_client.modules.users.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.roles.RoleEntity;
import br.com.autoinsight.autoinsight_client.modules.roles.useCases.RoleCachingUseCase;
import br.com.autoinsight.autoinsight_client.modules.users.UsersEntity;
import br.com.autoinsight.autoinsight_client.modules.users.dto.UpdateUserRequestDTO;
import br.com.autoinsight.autoinsight_client.modules.users.dto.UserResponseDTO;
import br.com.autoinsight.autoinsight_client.modules.users.mapper.UsersMapper;

@Service
public class UpdateUserUseCase {

  @Autowired
  private UsersCachingUseCase usersCachingUseCase;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UsersMapper usersMapper;

  @Autowired
  private RoleCachingUseCase roleCachingUseCase;

  public UserResponseDTO execute(String id, UpdateUserRequestDTO updateUserRequestDTO) {
    UsersEntity user = usersCachingUseCase.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (updateUserRequestDTO.getEmail() != null &&
        !updateUserRequestDTO.getEmail().equals(user.getEmail()) &&
        usersCachingUseCase.existsByEmail(updateUserRequestDTO.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    if (updateUserRequestDTO.getName() != null) {
      user.setName(updateUserRequestDTO.getName());
    }

    if (updateUserRequestDTO.getEmail() != null) {
      user.setEmail(updateUserRequestDTO.getEmail());
    }

    if (updateUserRequestDTO.getPassword() != null) {
      user.setPassword(passwordEncoder.encode(updateUserRequestDTO.getPassword()));
    }

    if (updateUserRequestDTO.getRoleId() != null && !updateUserRequestDTO.getRoleId().trim().isEmpty()) {
      RoleEntity role = roleCachingUseCase.findById(updateUserRequestDTO.getRoleId())
          .orElseThrow(() -> new RuntimeException("Role not found"));
      user.setRole(role);
    }

    UsersEntity updatedUser = usersCachingUseCase.save(user);

    return usersMapper.toResponseDTO(updatedUser);
  }
}