package br.com.autoinsight.autoinsight_client.modules.vehicles.dto;

import lombok.Data;

@Data
public class VehicleDTO {
  private String id;
  private String plate;
  private String modelId;
  private String userId;
}
