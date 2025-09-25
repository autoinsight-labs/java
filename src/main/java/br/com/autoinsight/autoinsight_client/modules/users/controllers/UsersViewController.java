package br.com.autoinsight.autoinsight_client.modules.users.controllers;

import java.util.List;

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

import br.com.autoinsight.autoinsight_client.modules.users.dto.CreateUserRequestDTO;
import br.com.autoinsight.autoinsight_client.modules.users.dto.UpdateUserRequestDTO;
import br.com.autoinsight.autoinsight_client.modules.users.dto.UserResponseDTO;
import br.com.autoinsight.autoinsight_client.modules.users.useCases.CreateUserUseCase;
import br.com.autoinsight.autoinsight_client.modules.users.useCases.DeleteUserUseCase;
import br.com.autoinsight.autoinsight_client.modules.users.useCases.GetAllUsersUseCase;
import br.com.autoinsight.autoinsight_client.modules.users.useCases.GetUserByIdUseCase;
import br.com.autoinsight.autoinsight_client.modules.users.useCases.UpdateUserUseCase;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/view/users")
public class UsersViewController {

  @Autowired
  private CreateUserUseCase createUserUseCase;

  @Autowired
  private GetAllUsersUseCase getAllUsersUseCase;

  @Autowired
  private GetUserByIdUseCase getUserByIdUseCase;

  @Autowired
  private UpdateUserUseCase updateUserUseCase;

  @Autowired
  private DeleteUserUseCase deleteUserUseCase;

  @GetMapping
  public ModelAndView listUsers() {
    ModelAndView modelAndView = new ModelAndView("users/list");

    try {
      Pageable pageable = PageRequest.of(0, 100); // Buscar até 100 usuários
      var usersPage = getAllUsersUseCase.execute(pageable);
      modelAndView.addObject("users", usersPage.getContent());
    } catch (Exception e) {
      modelAndView.addObject("users", List.of());
      modelAndView.addObject("errorMessage", "Erro ao carregar usuários: " + e.getMessage());
    }

    return modelAndView;
  }

  @GetMapping("/create")
  public ModelAndView createUserForm() {
    ModelAndView modelAndView = new ModelAndView("users/create");
    modelAndView.addObject("userRequest", new CreateUserRequestDTO());
    return modelAndView;
  }

  @PostMapping
  public String createUser(@Valid @ModelAttribute("userRequest") CreateUserRequestDTO userRequest,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      return "users/create";
    }

    try {
      createUserUseCase.execute(userRequest);
      redirectAttributes.addFlashAttribute("successMessage", "Usuário criado com sucesso!");
      return "redirect:/view/users";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar usuário: " + e.getMessage());
      return "redirect:/view/users/create";
    }
  }

  @GetMapping("/{id}/edit")
  public ModelAndView editUserForm(@PathVariable String id, RedirectAttributes redirectAttributes) {
    try {
      UserResponseDTO user = getUserByIdUseCase.execute(id);
      ModelAndView modelAndView = new ModelAndView("users/edit");
      modelAndView.addObject("user", user);
      modelAndView.addObject("userRequest", new UpdateUserRequestDTO());
      return modelAndView;
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Usuário não encontrado: " + e.getMessage());
      return new ModelAndView("redirect:/view/users");
    }
  }

  @PutMapping("/{id}")
  public String updateUser(@PathVariable String id,
      @Valid @ModelAttribute("userRequest") UpdateUserRequestDTO userRequest,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes,
      Model model) {
    if (bindingResult.hasErrors()) {
      try {
        UserResponseDTO user = getUserByIdUseCase.execute(id);
        model.addAttribute("user", user);
        return "users/edit";
      } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Usuário não encontrado: " + e.getMessage());
        return "redirect:/view/users";
      }
    }

    try {
      updateUserUseCase.execute(id, userRequest);
      redirectAttributes.addFlashAttribute("successMessage", "Usuário atualizado com sucesso!");
      return "redirect:/view/users";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar usuário: " + e.getMessage());
      return "redirect:/view/users/" + id + "/edit";
    }
  }

  @PostMapping("/{id}/delete")
  public String deleteUser(@PathVariable String id, RedirectAttributes redirectAttributes) {
    try {
      deleteUserUseCase.execute(id);
      redirectAttributes.addFlashAttribute("successMessage", "Usuário excluído com sucesso!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir usuário: " + e.getMessage());
    }
    return "redirect:/view/users";
  }
}
