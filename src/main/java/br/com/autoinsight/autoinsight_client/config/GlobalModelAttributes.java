package br.com.autoinsight.autoinsight_client.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.com.autoinsight.autoinsight_client.modules.users.UsersEntity;

@ControllerAdvice
public class GlobalModelAttributes {

  @ModelAttribute("currentUser")
  public UsersEntity addCurrentUserToModel() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UsersEntity) {
      return (UsersEntity) authentication.getPrincipal();
    }
    return null;
  }
}
