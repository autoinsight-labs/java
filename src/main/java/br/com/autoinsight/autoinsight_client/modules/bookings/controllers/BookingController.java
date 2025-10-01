package br.com.autoinsight.autoinsight_client.modules.bookings.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Reservas", description = "Endpoints para gerenciamento de reservas de motos nos pátios")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

  @Autowired
  private CreateBookingUseCase createBookingUseCase;

  @Autowired
  private UpdateBookingUseCase updateBookingUseCase;

  @Autowired
  private DeleteBookingUseCase deleteBookingUseCase;

  @Autowired
  private BookingCachingUseCase bookingCachingUseCase;

  @PostMapping
  @Operation(summary = "Criar nova reserva", description = "Registra uma nova reserva de moto em um pátio")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reserva criada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos ou reserva já existente", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
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

  @GetMapping
  @Operation(summary = "Listar todas as reservas", description = "Retorna uma lista paginada de todas as reservas cadastradas no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de reservas obtida com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public Page<BookingDTO> getAllBookings(
      @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("occursAt").descending());
    return bookingCachingUseCase.findAll(pageable)
        .map(BookingMapper::toDTO);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar reserva por ID", description = "Retorna os dados de uma reserva específica baseado no seu ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reserva encontrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))),
      @ApiResponse(responseCode = "404", description = "Reserva não encontrada", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> getBookingById(
      @Parameter(description = "ID único da reserva", required = true) @PathVariable String id) {
    Optional<BookingEntity> booking = bookingCachingUseCase.findById(id);
    if (booking.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
    }
    return ResponseEntity.ok(BookingMapper.toDTO(booking.get()));
  }

  @GetMapping("/yard/{yardId}")
  @Operation(summary = "Buscar reservas por pátio", description = "Retorna uma lista paginada de reservas filtradas por ID do pátio")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de reservas do pátio obtida com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public Page<BookingDTO> getBookingsByYardId(
      @Parameter(description = "ID único do pátio", required = true) @PathVariable String yardId,
      @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("occursAt").descending());
    return bookingCachingUseCase.findByYardId(yardId, pageable)
        .map(BookingMapper::toDTO);
  }

  @GetMapping("/vehicle/{vehicleId}")
  @Operation(summary = "Buscar reservas por veículo", description = "Retorna uma lista paginada de reservas filtradas por ID do veículo")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de reservas do veículo obtida com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public Page<BookingDTO> getBookingsByVehicleId(
      @Parameter(description = "ID único do veículo", required = true) @PathVariable String vehicleId,
      @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("occursAt").descending());
    return bookingCachingUseCase.findByVehicleId(vehicleId, pageable)
        .map(BookingMapper::toDTO);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar reserva", description = "Atualiza os dados de uma reserva existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Reserva atualizada com sucesso", content = @Content),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
      @ApiResponse(responseCode = "404", description = "Reserva não encontrada", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> updateBooking(
      @Parameter(description = "ID único da reserva", required = true) @PathVariable String id,
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
  @Operation(summary = "Excluir reserva", description = "Remove uma reserva do sistema permanentemente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Reserva excluída com sucesso", content = @Content),
      @ApiResponse(responseCode = "404", description = "Reserva não encontrada", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<Object> deleteBooking(
      @Parameter(description = "ID único da reserva", required = true) @PathVariable String id) {
    Optional<BookingEntity> existing = deleteBookingUseCase.findById(id);
    if (existing.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
    }
    deleteBookingUseCase.deleteById(id);
    bookingCachingUseCase.clearCache();
    return ResponseEntity.noContent().build();
  }
}
