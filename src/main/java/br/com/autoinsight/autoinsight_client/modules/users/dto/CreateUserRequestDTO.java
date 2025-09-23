package br.com.autoinsight.autoinsight_client.modules.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados necessários para criar um novo usuário no sistema")
public class CreateUserRequestDTO {

  @NotBlank(message = "Name is required")
  @Size(max = 150, message = "Name must not exceed 150 characters")
  @Schema(description = "Nome completo do usuário", example = "João Silva Santos")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  @Size(max = 150, message = "Email must not exceed 150 characters")
  @Schema(description = "Email único do usuário para login", example = "joao.silva@empresa.com")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
  @Schema(description = "Senha segura com pelo menos 6 caracteres", example = "minhasenha123")
  private String password;

  @Size(min = 24, max = 24, message = "RoleId must be 24 characters (CUID2)")
  @Schema(description = "ID da role associada ao usuário. Se omitido, usa a role padrão.", example = "2345656789abcdef01234567")
  private String roleId;
}