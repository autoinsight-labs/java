package br.com.autoinsight.autoinsight_client.modules.vehicles.useCases;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleEntity;
import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleRepository;

@Service
public class VehicleCachingUseCase {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Cacheable(value = "findAllVehicles")
    public List<VehicleEntity> findAll() {
        return vehicleRepository.findAll();
    }

    @Cacheable(value = "findVehicleById", key = "#id")
    public Optional<VehicleEntity> findById(String id) {
        return vehicleRepository.findById(id);
    }

    @Cacheable(value = "findVehicleByUserId", key = "#userId")
    public Optional<VehicleEntity> findByUserId(String userId) {
        return vehicleRepository.findByUserId(userId);
    }

    @Cacheable(value = "findAllVehiclesPaged", key = "#req")
    public Page<VehicleEntity> findAll(PageRequest req) {
        return vehicleRepository.findAll(req);
    }

    @CacheEvict(value = { "findAllVehicles", "findVehicleById", "findVehicleByUserId", "findAllVehiclesPaged" }, allEntries = true)
    public void clearCache() {
        System.out.println("Clearing vehicle cache!");
    }
}
