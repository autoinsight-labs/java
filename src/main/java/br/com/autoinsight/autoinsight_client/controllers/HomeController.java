package br.com.autoinsight.autoinsight_client.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.autoinsight.autoinsight_client.modules.bookings.useCases.BookingCachingUseCase;
import br.com.autoinsight.autoinsight_client.modules.roles.useCases.RoleCachingUseCase;
import br.com.autoinsight.autoinsight_client.modules.users.useCases.GetAllUsersUseCase;
import br.com.autoinsight.autoinsight_client.modules.vehicles.useCases.VehicleCachingUseCase;

@Controller
public class HomeController {

  @Autowired
  private GetAllUsersUseCase getAllUsersUseCase;

  @Autowired
  private VehicleCachingUseCase vehicleCachingUseCase;

  @Autowired
  private BookingCachingUseCase bookingCachingUseCase;

  @Autowired
  private RoleCachingUseCase roleCachingUseCase;

  @GetMapping("/")
  public String home(Model model) {
    try {
      // Contar usuários
      Pageable pageable = PageRequest.of(0, 1);
      long userCount = getAllUsersUseCase.execute(pageable).getTotalElements();
      model.addAttribute("userCount", userCount);

      // Contar veículos
      long vehicleCount = vehicleCachingUseCase.findAll(pageable).getTotalElements();
      model.addAttribute("vehicleCount", vehicleCount);

      // Contar reservas
      long bookingCount = bookingCachingUseCase.findAll(pageable).getTotalElements();
      model.addAttribute("bookingCount", bookingCount);

      // Contar roles
      long roleCount = roleCachingUseCase.findAll(pageable).getTotalElements();
      model.addAttribute("roleCount", roleCount);

    } catch (Exception e) {
      // Em caso de erro, definir contadores como 0
      model.addAttribute("userCount", 0);
      model.addAttribute("vehicleCount", 0);
      model.addAttribute("bookingCount", 0);
      model.addAttribute("roleCount", 0);
    }

    return "index";
  }
}
