package br.com.autoinsight.autoinsight_client.modules.bookings.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.autoinsight.autoinsight_client.modules.bookings.BookingEntity;
import br.com.autoinsight.autoinsight_client.modules.bookings.dto.BookingDTO;
import br.com.autoinsight.autoinsight_client.modules.bookings.mapper.BookingMapper;
import br.com.autoinsight.autoinsight_client.modules.bookings.useCases.BookingCachingUseCase;
import br.com.autoinsight.autoinsight_client.modules.bookings.useCases.CreateBookingUseCase;
import br.com.autoinsight.autoinsight_client.modules.bookings.useCases.DeleteBookingUseCase;
import br.com.autoinsight.autoinsight_client.modules.bookings.useCases.UpdateBookingUseCase;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/view/bookings")
public class BookingsViewController {

  @Autowired
  private CreateBookingUseCase createBookingUseCase;

  @Autowired
  private UpdateBookingUseCase updateBookingUseCase;

  @Autowired
  private DeleteBookingUseCase deleteBookingUseCase;

  @Autowired
  private BookingCachingUseCase bookingCachingUseCase;

  @GetMapping
  public ModelAndView listBookings() {
    ModelAndView modelAndView = new ModelAndView("bookings/list");
    try {
      Pageable pageable = PageRequest.of(0, 100);
      var page = bookingCachingUseCase.findAll(pageable);
      modelAndView.addObject("bookings", page.getContent().stream()
          .map(BookingMapper::toDTO)
          .toList());
    } catch (Exception e) {
      modelAndView.addObject("bookings", List.of());
      modelAndView.addObject("errorMessage", "Erro ao carregar reservas: " + e.getMessage());
    }
    return modelAndView;
  }

  @GetMapping("/create")
  public ModelAndView createBookingForm() {
    ModelAndView modelAndView = new ModelAndView("bookings/create");
    modelAndView.addObject("bookingRequest", new BookingDTO());
    return modelAndView;
  }

  @PostMapping
  public String createBooking(@Valid @ModelAttribute("bookingRequest") BookingDTO bookingRequest,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      return "bookings/create";
    }
    try {
      BookingEntity entity = BookingMapper.toEntity(bookingRequest);
      createBookingUseCase.execute(entity);
      bookingCachingUseCase.clearCache();
      redirectAttributes.addFlashAttribute("successMessage", "Reserva criada com sucesso!");
      return "redirect:/view/bookings";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar reserva: " + e.getMessage());
      return "redirect:/view/bookings/create";
    }
  }

  @GetMapping("/{id}/edit")
  public ModelAndView editBookingForm(@PathVariable String id, RedirectAttributes redirectAttributes) {
    try {
      Optional<BookingEntity> bookingOpt = bookingCachingUseCase.findById(id);
      if (bookingOpt.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Reserva não encontrada");
        return new ModelAndView("redirect:/view/bookings");
      }
      BookingDTO booking = BookingMapper.toDTO(bookingOpt.get());
      ModelAndView modelAndView = new ModelAndView("bookings/edit");
      modelAndView.addObject("booking", booking);
      modelAndView.addObject("bookingRequest", booking);
      return modelAndView;
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao carregar reserva: " + e.getMessage());
      return new ModelAndView("redirect:/view/bookings");
    }
  }

  @PutMapping("/{id}")
  public String updateBooking(@PathVariable String id,
      @Valid @ModelAttribute("bookingRequest") BookingDTO bookingRequest,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes,
      Model model) {
    if (bindingResult.hasErrors()) {
      try {
        Optional<BookingEntity> bookingOpt = bookingCachingUseCase.findById(id);
        if (bookingOpt.isPresent()) {
          BookingDTO booking = BookingMapper.toDTO(bookingOpt.get());
          model.addAttribute("booking", booking);
          return "bookings/edit";
        }
      } catch (Exception e) {
        // ignore and fallthrough to not found
      }
      redirectAttributes.addFlashAttribute("errorMessage", "Reserva não encontrada");
      return "redirect:/view/bookings";
    }

    try {
      Optional<BookingEntity> existing = updateBookingUseCase.findById(id);
      if (existing.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Reserva não encontrada");
        return "redirect:/view/bookings";
      }

      BookingEntity old = existing.get();
      if (bookingRequest.getVehicleId() != null)
        old.setVehicleId(bookingRequest.getVehicleId());
      if (bookingRequest.getYardId() != null)
        old.setYardId(bookingRequest.getYardId());
      if (bookingRequest.getOccursAt() != null)
        old.setOccursAt(bookingRequest.getOccursAt());
      if (bookingRequest.getCancelledAt() != null)
        old.setCancelledAt(bookingRequest.getCancelledAt());

      updateBookingUseCase.save(old);
      bookingCachingUseCase.clearCache();
      redirectAttributes.addFlashAttribute("successMessage", "Reserva atualizada com sucesso!");
      return "redirect:/view/bookings";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar reserva: " + e.getMessage());
      return "redirect:/view/bookings/" + id + "/edit";
    }
  }

  @PostMapping("/{id}/delete")
  public String deleteBooking(@PathVariable String id, RedirectAttributes redirectAttributes) {
    try {
      Optional<BookingEntity> existing = deleteBookingUseCase.findById(id);
      if (existing.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Reserva não encontrada");
        return "redirect:/view/bookings";
      }
      deleteBookingUseCase.deleteById(id);
      bookingCachingUseCase.clearCache();
      redirectAttributes.addFlashAttribute("successMessage", "Reserva excluída com sucesso!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir reserva: " + e.getMessage());
    }
    return "redirect:/view/bookings";
  }
}
