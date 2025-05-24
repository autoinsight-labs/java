package br.com.autoinsight.autoinsight_client.modules.vehicles.useCases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleEntity;
import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class DeleteVehicleUseCase {

  @Autowired
  private VehicleRepository vehicleRepository;

  public Optional<VehicleEntity> findById(String id) {
    return vehicleRepository.findById(id);
  }

  public void deleteById(String id) {
    try {
      vehicleRepository.deleteById(id);
    } catch (Exception e) {
      String errorMessage = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

      if (errorMessage.contains("yard_vehicle_vehicle_fk") || errorMessage.contains("vehicle_id")) {
        throw new EntityNotFoundException("Vehicle is associated with a yard and cannot be deleted!");
      }
      
      if (errorMessage.contains("booking_vehicle_fk")) {
        throw new EntityNotFoundException("Vehicle is associated with a booking and cannot be deleted!");
      }
      
      throw e;
    }
  }
}
