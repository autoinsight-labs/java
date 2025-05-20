package br.com.autoinsight.autoinsight_client.modules.vehicles.mapper;

import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleEntity;
import br.com.autoinsight.autoinsight_client.modules.vehicles.dto.VehicleDTO;
import io.github.thibaultmeyer.cuid.CUID;

public class VehicleMapper {
  public static VehicleDTO toDTO(VehicleEntity entity) {
    VehicleDTO dto = new VehicleDTO();
    dto.setId(entity.getId());
    dto.setPlate(entity.getPlate());
    dto.setModelId(entity.getModelId());
    dto.setUserId(entity.getUserId());
    return dto;
  }

  public static VehicleEntity toEntity(VehicleDTO dto) {
    VehicleEntity entity = new VehicleEntity();
    entity.setId(CUID.randomCUID2().toString());
    entity.setPlate(dto.getPlate());
    entity.setModelId(dto.getModelId());
    entity.setUserId(dto.getUserId());
    return entity;
  }
}
