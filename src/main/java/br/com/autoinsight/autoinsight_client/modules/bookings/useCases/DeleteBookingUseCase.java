package br.com.autoinsight.autoinsight_client.modules.bookings.useCases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.autoinsight.autoinsight_client.modules.bookings.BookingEntity;
import br.com.autoinsight.autoinsight_client.modules.bookings.BookingRepository;

@Service
public class DeleteBookingUseCase {

  @Autowired
  private BookingRepository bookingRepository;

  public Optional<BookingEntity> findById(String id) {
    return bookingRepository.findById(id);
  }

  public void deleteById(String id) {
    bookingRepository.deleteById(id);
  }
}
