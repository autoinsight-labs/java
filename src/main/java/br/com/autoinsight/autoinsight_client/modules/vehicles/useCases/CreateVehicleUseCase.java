package br.com.autoinsight.autoinsight_client.modules.vehicles.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.exceptions.PlateFoundException;
import br.com.autoinsight.autoinsight_client.exceptions.UserVehicleFoundException;
import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleEntity;
import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleRepository;

@Service
public class CreateVehicleUseCase {

  @Autowired
  private VehicleRepository vehicleRepository;

  public VehicleEntity execute(VehicleEntity vehicleEntity) {
    this.vehicleRepository
      .findByPlate(vehicleEntity.getPlate())
      .ifPresent((plate) -> {
        throw new PlateFoundException();
      });

    this.vehicleRepository
      .findByUserId(vehicleEntity.getUserId())
      .ifPresent((userId) -> {
        throw new UserVehicleFoundException();
      });

    return this.vehicleRepository.save(vehicleEntity);
  }

}
