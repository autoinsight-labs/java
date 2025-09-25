package br.com.autoinsight.autoinsight_client.modules.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.autoinsight.autoinsight_client.modules.auth.dto.LoginResponseDTO;
import br.com.autoinsight.autoinsight_client.modules.users.dto.CreateUserRequestDTO;
import br.com.autoinsight.autoinsight_client.modules.users.dto.UpdateUserRequestDTO;
import br.com.autoinsight.autoinsight_client.modules.users.dto.UserResponseDTO;
import br.com.autoinsight.autoinsight_client.modules.users.useCases.CreateUserUseCase;
import br.com.autoinsight.autoinsight_client.modules.users.useCases.DeleteUserUseCase;
import br.com.autoinsight.autoinsight_client.modules.users.useCases.GetAllUsersUseCase;
import br.com.autoinsight.autoinsight_client.modules.users.useCases.GetUserByIdUseCase;
import br.com.autoinsight.autoinsight_client.modules.users.useCases.UpdateUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários do sistema")
public class UsersController {

  @Autowired
  private CreateUserUseCase createUserUseCase;

  @Autowired
  private GetAllUsersUseCase getAllUsersUseCase;

  @Autowired
  private GetUserByIdUseCase getUserByIdUseCase;

  @Autowired
  private UpdateUserUseCase updateUserUseCase;

  @Autowired
  private DeleteUserUseCase deleteUserUseCase;

  @PostMapping("/register")
  @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário no sistema e retorna os dados do usuário criado junto com o token JWT")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos ou email já cadastrado", content = @Content)
  })
  public ResponseEntity<LoginResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
    try {
      LoginResponseDTO user = createUserUseCase.execute(createUserRequestDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(user);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping
  @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista paginada de todos os usuários cadastrados no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de usuários obtida com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
      @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
    Page<UserResponseDTO> users = getAllUsersUseCase.execute(pageable);
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico baseado no seu ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UserResponseDTO> getUserById(
      @Parameter(description = "ID único do usuário", required = true) @PathVariable String id) {
    try {
      UserResponseDTO user = getUserByIdUseCase.execute(id);
      return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UserResponseDTO> updateUser(
      @Parameter(description = "ID único do usuário", required = true) @PathVariable String id,
      @Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
    try {
      UserResponseDTO user = updateUserUseCase.execute(id, updateUserRequestDTO);
      return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema permanentemente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso", content = @Content),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "ID único do usuário", required = true) @PathVariable String id) {
    try {
      deleteUserUseCase.execute(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}