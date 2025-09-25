package br.com.autoinsight.autoinsight_client.modules.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.autoinsight.autoinsight_client.modules.auth.dto.LoginRequestDTO;
import br.com.autoinsight.autoinsight_client.modules.auth.dto.LoginResponseDTO;
import br.com.autoinsight.autoinsight_client.modules.auth.useCases.LoginUseCase;
import br.com.autoinsight.autoinsight_client.modules.users.UsersEntity;
import br.com.autoinsight.autoinsight_client.modules.users.dto.UserResponseDTO;
import br.com.autoinsight.autoinsight_client.modules.users.mapper.UsersMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e autorização de usuários")
public class AuthController {

  @Autowired
  private LoginUseCase loginUseCase;

  @Autowired
  private UsersMapper usersMapper;

  @PostMapping("/login")
  @Operation(summary = "Realizar login", description = "Autentica um usuário com email e senha, retornando um token JWT válido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))),
      @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content)
  })
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
    try {
      LoginResponseDTO response = loginUseCase.execute(loginRequestDTO);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @GetMapping("/me")
  @Operation(summary = "Obter dados do usuário autenticado", description = "Retorna as informações do usuário atualmente autenticado baseado no token JWT")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Dados do usuário obtidos com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UserResponseDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UsersEntity) {
      UsersEntity user = (UsersEntity) authentication.getPrincipal();
      UserResponseDTO userResponse = usersMapper.toResponseDTO(user);
      return ResponseEntity.ok(userResponse);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}