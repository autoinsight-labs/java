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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehicles")
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
  public Page<VehicleDTO> getAllVehicles(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return vehicleCachingUseCase.findAll(pageable)
        .map(VehicleMapper::toDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getVehicleById(@PathVariable String id) {
    Optional<VehicleEntity> vehicle = vehicleCachingUseCase.findById(id);
    if (vehicle.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
    }
    return ResponseEntity.ok(VehicleMapper.toDTO(vehicle.get()));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<Object> getVehiclesByUserId(@PathVariable String userId) {
    Optional<VehicleEntity> vehicle = vehicleCachingUseCase.findByUserId(userId);
    if (vehicle.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User vehicle not found!");
    }
    return ResponseEntity.ok(VehicleMapper.toDTO(vehicle.get()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> updateVehicle(@PathVariable String id,
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
  public ResponseEntity<Object> deleteVehicle(@PathVariable String id) {
    Optional<VehicleEntity> existing = deleteVehicleUseCase.findById(id);
    if (existing.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
    }
    deleteVehicleUseCase.deleteById(id);
    vehicleCachingUseCase.clearCache();
    return ResponseEntity.noContent().build();
  }
}
