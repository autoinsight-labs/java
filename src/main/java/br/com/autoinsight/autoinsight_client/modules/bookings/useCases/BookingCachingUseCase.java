package br.com.autoinsight.autoinsight_client.modules.bookings.useCases;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.bookings.BookingEntity;
import br.com.autoinsight.autoinsight_client.modules.bookings.BookingRepository;

@Service
public class BookingCachingUseCase {

  @Autowired
  private BookingRepository bookingRepository;

  @Cacheable(value = "findAllBookings")
  public List<BookingEntity> findAll() {
    return bookingRepository.findAll();
  }

  @Cacheable(value = "findBookingById", key = "#id")
  public Optional<BookingEntity> findById(String id) {
    return bookingRepository.findById(id);
  }

  @Cacheable(value =  "findByYardId", key = "#yardId")
  public List<BookingEntity> findByYardId(String yardId) {
    return bookingRepository.findByYardId(yardId);
  }

  @Cacheable(value = "findByVehicleId", key = "#vehicleId")
  public List<BookingEntity> findByVehicleId(String vehicleId) {
    return bookingRepository.findByVehicleId(vehicleId);
  }

  @Cacheable(value = "findAllBookingsPaged", key = "#req")
  public Page<BookingEntity> findAll(PageRequest req) {
    return bookingRepository.findAll(req);
  }

  @CacheEvict(value = { "findAllBookings", "findBookingById", "findByYardId", "findByVehicleId", "findAllBookingsPaged" }, allEntries = true)
  public void clearCache() {
    System.out.println("Clearing booking cache!");
  }
}
