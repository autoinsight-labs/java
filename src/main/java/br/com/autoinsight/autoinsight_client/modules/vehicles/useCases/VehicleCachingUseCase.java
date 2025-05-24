package br.com.autoinsight.autoinsight_client.modules.vehicles.useCases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleEntity;
import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleRepository;

@Service
public class VehicleCachingUseCase {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Cacheable(value = "findAllVehiclesPaged", key = "#pageable")
    public Page<VehicleEntity> findAll(Pageable pageable) {
        return vehicleRepository.findAll(pageable);
    }

    @Cacheable(value = "findVehicleById", key = "#id")
    public Optional<VehicleEntity> findById(String id) {
        return vehicleRepository.findById(id);
    }

    @Cacheable(value = "findVehicleByUserId", key = "#userId")
    public Optional<VehicleEntity> findByUserId(String userId) {
        return vehicleRepository.findByUserId(userId);
    }

    @CacheEvict(value = {
            "findAllVehiclesPaged",
            "findVehicleById",
            "findVehicleByUserId"
    }, allEntries = true)
    public void clearCache() {
        System.out.println("Clearing vehicle cache!");
    }
}
