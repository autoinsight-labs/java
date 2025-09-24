package br.com.autoinsight.autoinsight_client.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Dados necessários para realizar login no sistema")
public class LoginRequestDTO {

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  @Schema(description = "Email do usuário", example = "usuario@exemplo.com")
  private String email;

  @NotBlank(message = "Password is required")
  @Schema(description = "Senha do usuário", example = "minhasenha123")
  private String password;
} 