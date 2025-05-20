package br.com.autoinsight.autoinsight_client.modules.bookings.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BookingDTO {
  private String id;
  private String vehicleId;
  private String yardId;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime occursAt;
  
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime cancelledAt;
}
