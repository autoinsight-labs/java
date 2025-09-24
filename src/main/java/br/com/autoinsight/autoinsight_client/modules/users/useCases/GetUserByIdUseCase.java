package br.com.autoinsight.autoinsight_client.modules.users.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.users.UsersEntity;
import br.com.autoinsight.autoinsight_client.modules.users.dto.UserResponseDTO;
import br.com.autoinsight.autoinsight_client.modules.users.mapper.UsersMapper;

@Service
public class GetUserByIdUseCase {

  @Autowired
  private UsersCachingUseCase usersCachingUseCase;

  @Autowired
  private UsersMapper usersMapper;

  public UserResponseDTO execute(String id) {
    UsersEntity user = usersCachingUseCase.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    return usersMapper.toResponseDTO(user);
  }
}
