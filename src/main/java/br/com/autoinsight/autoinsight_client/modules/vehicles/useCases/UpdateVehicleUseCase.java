package br.com.autoinsight.autoinsight_client.modules.vehicles.useCases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleEntity;
import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleRepository;

@Service
public class UpdateVehicleUseCase {

  @Autowired
  private VehicleRepository vehicleRepository;

  public Optional<VehicleEntity> findById(String id) {
    return vehicleRepository.findById(id);
  }

  public void save(VehicleEntity vehicleEntity) {
    vehicleRepository.save(vehicleEntity);
  }
}
