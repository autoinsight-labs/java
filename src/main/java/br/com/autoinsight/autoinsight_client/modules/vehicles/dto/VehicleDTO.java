package br.com.autoinsight.autoinsight_client.modules.vehicles.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VehicleDTO {
  private String id;

  @NotNull(message = "Plate is required")
  @Pattern(regexp = "^[A-Z]{3}[0-9][0-9A-Z][0-9]{2}$", message = "Invalid Brazilian license plate format")
  private String plate;

  @NotNull(message = "Model ID is required")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for model ID")
  private String modelId;

  @NotNull(message = "User ID is required")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for user ID")
  private String userId;
}
