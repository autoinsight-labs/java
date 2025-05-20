package br.com.autoinsight.autoinsight_client.modules.bookings.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.autoinsight.autoinsight_client.modules.bookings.BookingEntity;
import br.com.autoinsight.autoinsight_client.modules.bookings.dto.BookingDTO;
import br.com.autoinsight.autoinsight_client.modules.bookings.mapper.BookingMapper;
import br.com.autoinsight.autoinsight_client.modules.bookings.useCases.BookingCachingUseCase;
import br.com.autoinsight.autoinsight_client.modules.bookings.useCases.CreateBookingUseCase;
import br.com.autoinsight.autoinsight_client.modules.bookings.useCases.DeleteBookingUseCase;
import br.com.autoinsight.autoinsight_client.modules.bookings.useCases.UpdateBookingUseCase;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/bookings")
public class BookingController {

  @Autowired
  private CreateBookingUseCase createBookingUseCase;

  @Autowired
  private UpdateBookingUseCase updateBookingUseCase;

  @Autowired
  private DeleteBookingUseCase deleteBookingUseCase;

  @Autowired
  private BookingCachingUseCase bookingCachingUseCase;

  @PostMapping("/")
  public ResponseEntity<Object> create(@Valid @RequestBody BookingDTO bookingDTO) {
    try {
      var entity = BookingMapper.toEntity(bookingDTO);
      var result = this.createBookingUseCase.execute(entity);
      bookingCachingUseCase.clearCache();
      return ResponseEntity.ok().body(BookingMapper.toDTO(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/")
  public List<BookingDTO> getAllBookings() {
    return bookingCachingUseCase.findAll()
        .stream()
        .map(BookingMapper::toDTO)
        .toList();
  }

  @GetMapping("/paged")
  public Page<BookingDTO> getPagedBookings(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    PageRequest pageable = PageRequest.of(page, size, Sort.by("occursAt").descending());
    return bookingCachingUseCase.findAll(pageable)
        .map(BookingMapper::toDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getBookingById(@PathVariable String id) {
    Optional<BookingEntity> booking = bookingCachingUseCase.findById(id);
    if (booking.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
    }
    return ResponseEntity.ok(BookingMapper.toDTO(booking.get()));
  }

  @GetMapping("/yard/{yardId}")
  public ResponseEntity<Object> getBookingsByYardId(@PathVariable String yardId) {
    List<BookingEntity> bookings = bookingCachingUseCase.findByYardId(yardId);
    if (bookings.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Yard bookings not found!");
    }
    List<BookingDTO> dtos = bookings.stream().map(BookingMapper::toDTO).toList();
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("vehicle/{vehicleId}")
  public ResponseEntity<Object> getBookingsByVehicleId(@PathVariable String vehicleId) {
    List<BookingEntity> bookings = bookingCachingUseCase.findByVehicleId(vehicleId);
    if (bookings.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle bookings not found!");
    }
    List<BookingDTO> dtos = bookings.stream().map(BookingMapper::toDTO).toList();
    return ResponseEntity.ok(dtos);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> updateBooking(@PathVariable String id,
      @Valid @RequestBody BookingDTO bookingDTO) {
    Optional<BookingEntity> existing = updateBookingUseCase.findById(id);
    if (existing.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
    }
    BookingEntity oldBooking = existing.get();

    if (bookingDTO.getVehicleId() != null) {
      oldBooking.setVehicleId(bookingDTO.getVehicleId());
    }
    if (bookingDTO.getYardId() != null) {
      oldBooking.setYardId(bookingDTO.getYardId());
    }
    if (bookingDTO.getOccursAt() != null) {
      oldBooking.setOccursAt(bookingDTO.getOccursAt());
    }
    if (bookingDTO.getCancelledAt() != null) {
      oldBooking.setCancelledAt(bookingDTO.getCancelledAt());
    }

    updateBookingUseCase.save(oldBooking);
    bookingCachingUseCase.clearCache();

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteBooking(@PathVariable String id) {
    Optional<BookingEntity> existing = deleteBookingUseCase.findById(id);
    if (existing.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
    }
    deleteBookingUseCase.deleteById(id);
    bookingCachingUseCase.clearCache();
    return ResponseEntity.noContent().build();
  }
}
