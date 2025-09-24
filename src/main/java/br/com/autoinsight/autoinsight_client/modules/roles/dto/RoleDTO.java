package br.com.autoinsight.autoinsight_client.modules.roles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleDTO {
  private String id;

  @NotBlank(message = "Name is required")
  @Size(max = 50, message = "Name must not exceed 50 characters")
  private String name;

  @NotBlank(message = "Acronym is required")
  @Size(max = 10, message = "Acronym must not exceed 10 characters")
  private String acronym;
}
