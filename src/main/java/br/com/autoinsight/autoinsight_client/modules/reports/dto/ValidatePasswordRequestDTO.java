package br.com.autoinsight.autoinsight_client.modules.reports.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidatePasswordRequestDTO {

  @NotBlank(message = "Password is required")
  private String password;
}
