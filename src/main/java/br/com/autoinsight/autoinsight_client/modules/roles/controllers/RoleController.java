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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/roles")
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
  public ResponseEntity<Object> create(@Valid @RequestBody RoleDTO dto) {
    try {
      var result = this.createRoleUseCase.execute(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping
  public Page<RoleDTO> getAll(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return roleCachingUseCase.findAll(pageable).map(RoleMapper::toDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getById(@PathVariable String id) {
    var role = roleCachingUseCase.findById(id);
    if (role.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
    }
    return ResponseEntity.ok(RoleMapper.toDTO(role.get()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable String id, @Valid @RequestBody RoleDTO dto) {
    try {
      var result = updateRoleUseCase.execute(id, dto);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable String id) {
    var existing = roleCachingUseCase.findById(id);
    if (existing.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
    }
    deleteRoleUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }
}
