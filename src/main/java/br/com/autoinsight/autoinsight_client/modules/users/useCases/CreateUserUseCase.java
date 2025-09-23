package br.com.autoinsight.autoinsight_client.modules.users.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.auth.dto.LoginResponseDTO;
import br.com.autoinsight.autoinsight_client.modules.auth.services.JwtService;
import br.com.autoinsight.autoinsight_client.modules.users.UsersEntity;
import br.com.autoinsight.autoinsight_client.modules.users.dto.CreateUserRequestDTO;
import br.com.autoinsight.autoinsight_client.modules.users.mapper.UsersMapper;
import io.github.thibaultmeyer.cuid.CUID;

@Service
public class CreateUserUseCase {

  @Autowired
  private UsersCachingUseCase usersCachingUseCase;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UsersMapper usersMapper;

  @Autowired
  private JwtService jwtService;

  public LoginResponseDTO execute(CreateUserRequestDTO createUserRequestDTO) {
    if (usersCachingUseCase.existsByEmail(createUserRequestDTO.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    UsersEntity user = new UsersEntity();
    user.setId(CUID.randomCUID2(24).toString());
    user.setName(createUserRequestDTO.getName());
    user.setEmail(createUserRequestDTO.getEmail());
    user.setPassword(passwordEncoder.encode(createUserRequestDTO.getPassword()));

    UsersEntity savedUser = usersCachingUseCase.save(user);

    String token = jwtService.generateToken(savedUser);

    return new LoginResponseDTO(token, usersMapper.toResponseDTO(savedUser));
  }
}