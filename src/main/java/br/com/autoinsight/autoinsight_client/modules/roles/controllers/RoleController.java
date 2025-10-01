package br.com.autoinsight.autoinsight_client.modules.roles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.autoinsight.autoinsight_client.modules.roles.dto.RoleDTO;
import br.com.autoinsight.autoinsight_client.modules.roles.mapper.RoleMapper;
import br.com.autoinsight.autoinsight_client.modules.roles.useCases.CreateRoleUseCase;
import br.com.autoinsight.autoinsight_client.modules.roles.useCases.DeleteRoleUseCase;
import br.com.autoinsight.autoinsight_client.modules.roles.useCases.RoleCachingUseCase;
import br.com.autoinsight.autoinsight_client.modules.roles.useCases.UpdateRoleUseCase;
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
@RequestMapping("/api/roles")
@Tag(name = "Papéis/Perfis", description = "Endpoints para gerenciamento de papéis e permissões de usuários")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

  @Autowired
  private CreateRoleUseCase createRoleUseCase;

  @Autowired
  private UpdateRoleUseCase updateRoleUseCase;

  @Autowired
  private DeleteRoleUseCase deleteRoleUseCase;

  @Autowired
  private RoleCachingUseCase roleCachingUseCase;

  @PostMapping
  @Operation(summary = "Criar novo papel", description = "Cria um novo papel/perfil no sistema com permissões específicas")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Papel criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos ou papel já existente", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> create(@Valid @RequestBody RoleDTO dto) {
    try {
      var result = this.createRoleUseCase.execute(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping
  @Operation(summary = "Listar todos os papéis", description = "Retorna uma lista paginada de todos os papéis/perfis cadastrados no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de papéis obtida com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public Page<RoleDTO> getAll(
      @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return roleCachingUseCase.findAll(pageable).map(RoleMapper::toDTO);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar papel por ID", description = "Retorna os dados de um papel específico baseado no seu ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Papel encontrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))),
      @ApiResponse(responseCode = "404", description = "Papel não encontrado", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> getById(
      @Parameter(description = "ID único do papel", required = true) @PathVariable String id) {
    var role = roleCachingUseCase.findById(id);
    if (role.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
    }
    return ResponseEntity.ok(RoleMapper.toDTO(role.get()));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar papel", description = "Atualiza os dados de um papel existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Papel atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
      @ApiResponse(responseCode = "404", description = "Papel não encontrado", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> update(
      @Parameter(description = "ID único do papel", required = true) @PathVariable String id,
      @Valid @RequestBody RoleDTO dto) {
    try {
      var result = updateRoleUseCase.execute(id, dto);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Excluir papel", description = "Remove um papel do sistema permanentemente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Papel excluído com sucesso", content = @Content),
      @ApiResponse(responseCode = "404", description = "Papel não encontrado", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> delete(
      @Parameter(description = "ID único do papel", required = true) @PathVariable String id) {
    var existing = roleCachingUseCase.findById(id);
    if (existing.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
    }
    deleteRoleUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }
}
