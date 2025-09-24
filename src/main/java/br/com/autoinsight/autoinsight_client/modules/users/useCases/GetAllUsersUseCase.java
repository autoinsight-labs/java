package br.com.autoinsight.autoinsight_client.modules.users.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.users.UsersEntity;
import br.com.autoinsight.autoinsight_client.modules.users.dto.UserResponseDTO;
import br.com.autoinsight.autoinsight_client.modules.users.mapper.UsersMapper;

@Service
public class GetAllUsersUseCase {

  @Autowired
  private UsersCachingUseCase usersCachingUseCase;

  @Autowired
  private UsersMapper usersMapper;

  public Page<UserResponseDTO> execute(Pageable pageable) {
    Page<UsersEntity> users = usersCachingUseCase.findAll(pageable);

    return users.map(usersMapper::toResponseDTO);
  }
}