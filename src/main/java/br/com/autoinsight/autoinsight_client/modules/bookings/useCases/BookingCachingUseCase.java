package br.com.autoinsight.autoinsight_client.modules.bookings.useCases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.bookings.BookingEntity;
import br.com.autoinsight.autoinsight_client.modules.bookings.BookingRepository;

@Service
public class BookingCachingUseCase {

  @Autowired
  private BookingRepository bookingRepository;

  @Cacheable(value = "findAllBookingsPaged", key = "#pageable")
  public Page<BookingEntity> findAll(Pageable pageable) {
    return bookingRepository.findAll(pageable);
  }

  @Cacheable(value = "findBookingById", key = "#id")
  public Optional<BookingEntity> findById(String id) {
    return bookingRepository.findById(id);
  }

  @Cacheable(value = "findByYardIdPaged", key = "#yardId + '_' + #pageable")
  public Page<BookingEntity> findByYardId(String yardId, Pageable pageable) {
    return bookingRepository.findByYardId(yardId, pageable);
  }

  @Cacheable(value = "findByVehicleIdPaged", key = "#vehicleId + '_' + #pageable")
  public Page<BookingEntity> findByVehicleId(String vehicleId, Pageable pageable) {
    return bookingRepository.findByVehicleId(vehicleId, pageable);
  }

  @CacheEvict(value = {
      "findAllBookingsPaged",
      "findBookingById",
      "findByYardIdPaged",
      "findByVehicleIdPaged"
  }, allEntries = true)
  public void clearCache() {
    System.out.println("Clearing booking cache!");
  }
}
