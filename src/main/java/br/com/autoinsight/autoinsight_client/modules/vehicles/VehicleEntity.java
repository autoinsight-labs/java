package br.com.autoinsight.autoinsight_client.modules.vehicles;

import io.github.thibaultmeyer.cuid.CUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicles")
public class VehicleEntity {

  @Id
  private String id;

  @Pattern(regexp = "^[A-Z]{3}[0-9][0-9A-Z][0-9]{2}$", message = "Invalid Brazilian license plate format")
  @Column(name = "plate", nullable = false, unique = true)
  private String plate;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  @Column(name = "model_id", nullable = false)
  private String modelId;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  @Column(name = "user_id", nullable = false)
  private String userId;

  public VehicleEntity() {
    this.id = CUID.randomCUID2().toString();
  }
}
