package br.com.autoinsight.autoinsight_client.modules.bookings.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.exceptions.BookingFoundException;
import br.com.autoinsight.autoinsight_client.modules.bookings.BookingEntity;
import br.com.autoinsight.autoinsight_client.modules.bookings.BookingRepository;
import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CreateBookingUseCase {

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private VehicleRepository vehicleRepository;

  public BookingEntity execute(BookingEntity bookingEntity) {

    this.bookingRepository
    .findByVehicleIdAndOccursAt(bookingEntity.getVehicleId(), bookingEntity.getOccursAt())
    .ifPresent((booking) -> {
      throw new BookingFoundException();
    });

    if (!this.vehicleRepository.existsById(bookingEntity.getVehicleId())) {
      throw new EntityNotFoundException("Vehicle not found!");
    }

    try {
      return this.bookingRepository.save(bookingEntity);
    } catch (Exception e) {
      String errorMessage = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

      if (errorMessage.contains("booking_yard_fk") || errorMessage.contains("yard_id")) {
        throw new EntityNotFoundException("Yard not found!");
      }
      throw e;
    }
  }

}
