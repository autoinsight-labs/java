package br.com.autoinsight.autoinsight_client.modules.bookings;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleEntity;
import io.github.thibaultmeyer.cuid.CUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name = "bookings")
public class BookingEntity {

  @Id
  private String id;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "vehicle_id", insertable = false, updatable = false)
  private VehicleEntity vehicleEntity;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  @Column(name = "vehicle_id", nullable = false)
  private String vehicleId;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  @Column(name = "yard_id", nullable = false)
  private String yardId;

  @Column(name = "occurs_at", nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @Future(message = "Must be a future date")
  private LocalDateTime occursAt;

  @Column(name = "cancelled_at", nullable = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @PastOrPresent(message = "Must be a past or present date")
  private LocalDateTime cancelledAt;

  public BookingEntity() {
    this.id = CUID.randomCUID2().toString();
  }

}
