package br.com.autoinsight.autoinsight_client.modules.vehicles.controllers;

import java.util.Optional;

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

import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleEntity;
import br.com.autoinsight.autoinsight_client.modules.vehicles.dto.VehicleDTO;
import br.com.autoinsight.autoinsight_client.modules.vehicles.mapper.VehicleMapper;
import br.com.autoinsight.autoinsight_client.modules.vehicles.useCases.CreateVehicleUseCase;
import br.com.autoinsight.autoinsight_client.modules.vehicles.useCases.DeleteVehicleUseCase;
import br.com.autoinsight.autoinsight_client.modules.vehicles.useCases.UpdateVehicleUseCase;
import br.com.autoinsight.autoinsight_client.modules.vehicles.useCases.VehicleCachingUseCase;
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
@RequestMapping("/api/vehicles")
@Tag(name = "Veículos", description = "Endpoints para gerenciamento de veículos (motos)")
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {

  @Autowired
  private CreateVehicleUseCase createVehicleUseCase;

  @Autowired
  private UpdateVehicleUseCase updateVehicleUseCase;

  @Autowired
  private DeleteVehicleUseCase deleteVehicleUseCase;

  @Autowired
  private VehicleCachingUseCase vehicleCachingUseCase;

  @PostMapping
  @Operation(summary = "Criar novo veículo", description = "Registra um novo veículo (moto) no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Veículo criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehicleDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos ou placa já cadastrada", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> create(@Valid @RequestBody VehicleDTO vehicleDTO) {
    try {
      var entity = VehicleMapper.toEntity(vehicleDTO);
      var result = this.createVehicleUseCase.execute(entity);
      vehicleCachingUseCase.clearCache();
      return ResponseEntity.ok().body(VehicleMapper.toDTO(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping
  @Operation(summary = "Listar todos os veículos", description = "Retorna uma lista paginada de todos os veículos cadastrados no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de veículos obtida com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public Page<VehicleDTO> getAllVehicles(
      @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return vehicleCachingUseCase.findAll(pageable)
        .map(VehicleMapper::toDTO);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar veículo por ID", description = "Retorna os dados de um veículo específico baseado no seu ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Veículo encontrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehicleDTO.class))),
      @ApiResponse(responseCode = "404", description = "Veículo não encontrado", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> getVehicleById(
      @Parameter(description = "ID único do veículo", required = true) @PathVariable String id) {
    Optional<VehicleEntity> vehicle = vehicleCachingUseCase.findById(id);
    if (vehicle.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
    }
    return ResponseEntity.ok(VehicleMapper.toDTO(vehicle.get()));
  }

  @GetMapping("/user/{userId}")
  @Operation(summary = "Buscar veículo por ID do usuário", description = "Retorna o veículo associado a um usuário específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Veículo do usuário encontrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehicleDTO.class))),
      @ApiResponse(responseCode = "404", description = "Veículo do usuário não encontrado", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> getVehiclesByUserId(
      @Parameter(description = "ID único do usuário", required = true) @PathVariable String userId) {
    Optional<VehicleEntity> vehicle = vehicleCachingUseCase.findByUserId(userId);
    if (vehicle.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User vehicle not found!");
    }
    return ResponseEntity.ok(VehicleMapper.toDTO(vehicle.get()));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar veículo", description = "Atualiza os dados de um veículo existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Veículo atualizado com sucesso", content = @Content),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
      @ApiResponse(responseCode = "404", description = "Veículo não encontrado", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> updateVehicle(
      @Parameter(description = "ID único do veículo", required = true) @PathVariable String id,
      @Valid @RequestBody VehicleDTO vehicleDTO) {
    try {
      Optional<VehicleEntity> existing = updateVehicleUseCase.findById(id);
      if (existing.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
      }
      VehicleEntity oldVehicle = existing.get();

      if (vehicleDTO.getModelId() != null) {
        oldVehicle.setModelId(vehicleDTO.getModelId());
      }
      if (vehicleDTO.getPlate() != null) {
        oldVehicle.setPlate(vehicleDTO.getPlate());
      }
      if (vehicleDTO.getUserId() != null) {
        oldVehicle.setUserId(vehicleDTO.getUserId());
      }

      updateVehicleUseCase.save(oldVehicle);
      vehicleCachingUseCase.clearCache();

      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Excluir veículo", description = "Remove um veículo do sistema permanentemente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Veículo excluído com sucesso", content = @Content),
      @ApiResponse(responseCode = "404", description = "Veículo não encontrado", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> deleteVehicle(
      @Parameter(description = "ID único do veículo", required = true) @PathVariable String id) {
    Optional<VehicleEntity> existing = deleteVehicleUseCase.findById(id);
    if (existing.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
    }
    deleteVehicleUseCase.deleteById(id);
    vehicleCachingUseCase.clearCache();
    return ResponseEntity.noContent().build();
  }
}
