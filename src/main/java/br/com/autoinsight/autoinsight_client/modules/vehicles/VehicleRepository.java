package br.com.autoinsight.autoinsight_client.modules.vehicles;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleEntity, String> {
  Optional<VehicleEntity> findByPlate(String plate);

  Optional<VehicleEntity> findByUserId(String userId);
}
