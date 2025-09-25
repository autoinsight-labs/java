package br.com.autoinsight.autoinsight_client.modules.vehicles.controllers;

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

import br.com.autoinsight.autoinsight_client.modules.vehicles.VehicleEntity;
import br.com.autoinsight.autoinsight_client.modules.vehicles.dto.VehicleDTO;
import br.com.autoinsight.autoinsight_client.modules.vehicles.mapper.VehicleMapper;
import br.com.autoinsight.autoinsight_client.modules.vehicles.useCases.CreateVehicleUseCase;
import br.com.autoinsight.autoinsight_client.modules.vehicles.useCases.DeleteVehicleUseCase;
import br.com.autoinsight.autoinsight_client.modules.vehicles.useCases.UpdateVehicleUseCase;
import br.com.autoinsight.autoinsight_client.modules.vehicles.useCases.VehicleCachingUseCase;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/view/vehicles")
public class VehicleViewController {

  @Autowired
  private CreateVehicleUseCase createVehicleUseCase;

  @Autowired
  private UpdateVehicleUseCase updateVehicleUseCase;

  @Autowired
  private DeleteVehicleUseCase deleteVehicleUseCase;

  @Autowired
  private VehicleCachingUseCase vehicleCachingUseCase;

  @GetMapping
  public ModelAndView listVehicles() {
    ModelAndView modelAndView = new ModelAndView("vehicles/list");

    try {
      Pageable pageable = PageRequest.of(0, 100); // Buscar até 100 veículos
      var vehiclesPage = vehicleCachingUseCase.findAll(pageable);
      modelAndView.addObject("vehicles", vehiclesPage.getContent().stream()
          .map(VehicleMapper::toDTO)
          .toList());
    } catch (Exception e) {
      modelAndView.addObject("vehicles", List.of());
      modelAndView.addObject("errorMessage", "Erro ao carregar veículos: " + e.getMessage());
    }

    return modelAndView;
  }

  @GetMapping("/create")
  public ModelAndView createVehicleForm() {
    ModelAndView modelAndView = new ModelAndView("vehicles/create");
    modelAndView.addObject("vehicleRequest", new VehicleDTO());
    return modelAndView;
  }

  @PostMapping
  public String createVehicle(@Valid @ModelAttribute("vehicleRequest") VehicleDTO vehicleRequest,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      return "vehicles/create";
    }

    try {
      var entity = VehicleMapper.toEntity(vehicleRequest);
      createVehicleUseCase.execute(entity);
      vehicleCachingUseCase.clearCache();
      redirectAttributes.addFlashAttribute("successMessage", "Veículo cadastrado com sucesso!");
      return "redirect:/view/vehicles";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cadastrar veículo: " + e.getMessage());
      return "redirect:/view/vehicles/create";
    }
  }

  @GetMapping("/{id}/edit")
  public ModelAndView editVehicleForm(@PathVariable String id, RedirectAttributes redirectAttributes) {
    try {
      Optional<VehicleEntity> vehicleOpt = vehicleCachingUseCase.findById(id);
      if (vehicleOpt.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Veículo não encontrado");
        return new ModelAndView("redirect:/view/vehicles");
      }

      VehicleDTO vehicle = VehicleMapper.toDTO(vehicleOpt.get());
      ModelAndView modelAndView = new ModelAndView("vehicles/edit");
      modelAndView.addObject("vehicle", vehicle);
      modelAndView.addObject("vehicleRequest", vehicle);
      return modelAndView;
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao carregar veículo: " + e.getMessage());
      return new ModelAndView("redirect:/view/vehicles");
    }
  }

  @PutMapping("/{id}")
  public String updateVehicle(@PathVariable String id,
      @Valid @ModelAttribute("vehicleRequest") VehicleDTO vehicleRequest,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes,
      Model model) {
    if (bindingResult.hasErrors()) {
      try {
        Optional<VehicleEntity> vehicleOpt = vehicleCachingUseCase.findById(id);
        if (vehicleOpt.isPresent()) {
          VehicleDTO vehicle = VehicleMapper.toDTO(vehicleOpt.get());
          model.addAttribute("vehicle", vehicle);
          return "vehicles/edit";
        }
      } catch (Exception e) {
        // Ignore
      }
      redirectAttributes.addFlashAttribute("errorMessage", "Veículo não encontrado");
      return "redirect:/view/vehicles";
    }

    try {
      Optional<VehicleEntity> existing = updateVehicleUseCase.findById(id);
      if (existing.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Veículo não encontrado");
        return "redirect:/view/vehicles";
      }

      VehicleEntity oldVehicle = existing.get();
      if (vehicleRequest.getModelId() != null) {
        oldVehicle.setModelId(vehicleRequest.getModelId());
      }
      if (vehicleRequest.getPlate() != null) {
        oldVehicle.setPlate(vehicleRequest.getPlate());
      }
      if (vehicleRequest.getUserId() != null) {
        oldVehicle.setUserId(vehicleRequest.getUserId());
      }

      updateVehicleUseCase.save(oldVehicle);
      vehicleCachingUseCase.clearCache();
      redirectAttributes.addFlashAttribute("successMessage", "Veículo atualizado com sucesso!");
      return "redirect:/view/vehicles";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar veículo: " + e.getMessage());
      return "redirect:/view/vehicles/" + id + "/edit";
    }
  }

  @PostMapping("/{id}/delete")
  public String deleteVehicle(@PathVariable String id, RedirectAttributes redirectAttributes) {
    try {
      Optional<VehicleEntity> existing = deleteVehicleUseCase.findById(id);
      if (existing.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Veículo não encontrado");
        return "redirect:/view/vehicles";
      }

      deleteVehicleUseCase.deleteById(id);
      vehicleCachingUseCase.clearCache();
      redirectAttributes.addFlashAttribute("successMessage", "Veículo excluído com sucesso!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir veículo: " + e.getMessage());
    }
    return "redirect:/view/vehicles";
  }
}
