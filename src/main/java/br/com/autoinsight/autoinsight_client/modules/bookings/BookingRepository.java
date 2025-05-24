package br.com.autoinsight.autoinsight_client.modules.bookings;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, String> {
  Optional<BookingEntity> findByVehicleIdAndOccursAt(String vehicleId, LocalDateTime occursAt);
  
  Page<BookingEntity> findByYardId(String yardId, Pageable pageable);
  
  Page<BookingEntity> findByVehicleId(String vehicleId, Pageable pageable);
}
