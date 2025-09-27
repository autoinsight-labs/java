package br.com.autoinsight.autoinsight_client.modules.roles.controllers;

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

import br.com.autoinsight.autoinsight_client.modules.roles.RoleEntity;
import br.com.autoinsight.autoinsight_client.modules.roles.dto.RoleDTO;
import br.com.autoinsight.autoinsight_client.modules.roles.mapper.RoleMapper;
import br.com.autoinsight.autoinsight_client.modules.roles.useCases.CreateRoleUseCase;
import br.com.autoinsight.autoinsight_client.modules.roles.useCases.DeleteRoleUseCase;
import br.com.autoinsight.autoinsight_client.modules.roles.useCases.RoleCachingUseCase;
import br.com.autoinsight.autoinsight_client.modules.roles.useCases.UpdateRoleUseCase;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/view/roles")
public class RoleViewController {

  @Autowired
  private CreateRoleUseCase createRoleUseCase;

  @Autowired
  private UpdateRoleUseCase updateRoleUseCase;

  @Autowired
  private DeleteRoleUseCase deleteRoleUseCase;

  @Autowired
  private RoleCachingUseCase roleCachingUseCase;

  @GetMapping
  public ModelAndView listRoles() {
    ModelAndView modelAndView = new ModelAndView("roles/list");

    try {
      Pageable pageable = PageRequest.of(0, 100);
      var rolesPage = roleCachingUseCase.findAll(pageable);
      modelAndView.addObject("roles", rolesPage.getContent().stream()
          .map(RoleMapper::toDTO)
          .toList());
    } catch (Exception e) {
      modelAndView.addObject("roles", List.of());
      modelAndView.addObject("errorMessage", "Erro ao carregar roles: " + e.getMessage());
    }

    return modelAndView;
  }

  @GetMapping("/create")
  public ModelAndView createRoleForm() {
    ModelAndView modelAndView = new ModelAndView("roles/create");
    modelAndView.addObject("roleRequest", new RoleDTO());
    return modelAndView;
  }

  @PostMapping
  public String createRole(@Valid @ModelAttribute("roleRequest") RoleDTO roleRequest,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      return "roles/create";
    }

    try {
      createRoleUseCase.execute(roleRequest);
      redirectAttributes.addFlashAttribute("successMessage", "Role criada com sucesso!");
      return "redirect:/view/roles";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar role: " + e.getMessage());
      return "redirect:/view/roles/create";
    }
  }

  @GetMapping("/{id}/edit")
  public ModelAndView editRoleForm(@PathVariable String id, RedirectAttributes redirectAttributes) {
    try {
      Optional<RoleEntity> roleOpt = roleCachingUseCase.findById(id);
      if (roleOpt.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Role não encontrada");
        return new ModelAndView("redirect:/view/roles");
      }

      RoleDTO role = RoleMapper.toDTO(roleOpt.get());
      ModelAndView modelAndView = new ModelAndView("roles/edit");
      modelAndView.addObject("role", role);
      modelAndView.addObject("roleRequest", role);
      return modelAndView;
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao carregar role: " + e.getMessage());
      return new ModelAndView("redirect:/view/roles");
    }
  }

  @PutMapping("/{id}")
  public String updateRole(@PathVariable String id,
      @Valid @ModelAttribute("roleRequest") RoleDTO roleRequest,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes,
      Model model) {
    if (bindingResult.hasErrors()) {
      try {
        Optional<RoleEntity> roleOpt = roleCachingUseCase.findById(id);
        if (roleOpt.isPresent()) {
          RoleDTO role = RoleMapper.toDTO(roleOpt.get());
          model.addAttribute("role", role);
          return "roles/edit";
        }
      } catch (Exception e) {
      }
      redirectAttributes.addFlashAttribute("errorMessage", "Role não encontrada");
      return "redirect:/view/roles";
    }

    try {
      updateRoleUseCase.execute(id, roleRequest);
      redirectAttributes.addFlashAttribute("successMessage", "Role atualizada com sucesso!");
      return "redirect:/view/roles";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar role: " + e.getMessage());
      return "redirect:/view/roles/" + id + "/edit";
    }
  }

  @PostMapping("/{id}/delete")
  public String deleteRole(@PathVariable String id, RedirectAttributes redirectAttributes) {
    try {
      Optional<RoleEntity> existing = roleCachingUseCase.findById(id);
      if (existing.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Role não encontrada");
        return "redirect:/view/roles";
      }

      deleteRoleUseCase.execute(id);
      redirectAttributes.addFlashAttribute("successMessage", "Role excluída com sucesso!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir role: " + e.getMessage());
    }
    return "redirect:/view/roles";
  }
}
