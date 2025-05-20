package br.com.autoinsight.autoinsight_client.modules.bookings.mapper;

import br.com.autoinsight.autoinsight_client.modules.bookings.BookingEntity;
import br.com.autoinsight.autoinsight_client.modules.bookings.dto.BookingDTO;
import io.github.thibaultmeyer.cuid.CUID;

public class BookingMapper {
  public static BookingDTO toDTO(BookingEntity entity) {
    BookingDTO dto = new BookingDTO();
    dto.setId(entity.getId());
    dto.setVehicleId(entity.getVehicleId());
    dto.setYardId(entity.getYardId());
    dto.setOccursAt(entity.getOccursAt());
    dto.setCancelledAt(entity.getCancelledAt());
    return dto;
  }

  public static BookingEntity toEntity(BookingDTO dto) {
    BookingEntity entity = new BookingEntity();
    entity.setId(CUID.randomCUID2().toString());
    entity.setVehicleId(dto.getVehicleId());
    entity.setYardId(dto.getYardId());
    entity.setOccursAt(dto.getOccursAt());
    entity.setCancelledAt(dto.getCancelledAt());
    return entity;
  }
}