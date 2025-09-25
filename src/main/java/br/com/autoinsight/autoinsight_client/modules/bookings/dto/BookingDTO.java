package br.com.autoinsight.autoinsight_client.modules.bookings.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BookingDTO {
  private String id;

  @NotNull(message = "Vehicle ID is required")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for vehicle ID")
  private String vehicleId;

  @NotNull(message = "Yard ID is required")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for yard ID")
  private String yardId;

  @NotNull(message = "Occurs at is required")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  @Future(message = "Must be a future date")
  private LocalDateTime occursAt;

  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  @PastOrPresent(message = "Must be a past or present date")
  private LocalDateTime cancelledAt;
}
