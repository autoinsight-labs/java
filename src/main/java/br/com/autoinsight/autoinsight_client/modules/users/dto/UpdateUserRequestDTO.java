package br.com.autoinsight.autoinsight_client.modules.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequestDTO {

  @Size(max = 150, message = "Name must not exceed 150 characters")
  private String name;

  @Email(message = "Invalid email format")
  @Size(max = 150, message = "Email must not exceed 150 characters")
  private String email;

  @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
  private String password;

  @Size(min = 24, max = 24, message = "RoleId must be 24 characters (CUID2)")
  private String roleId;
}