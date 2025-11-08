package br.com.autoinsight.autoinsight_client.modules.reports.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RelationalToJsonRequestDTO {

  @NotBlank(message = "Table name is required")
  private String table;

  @NotBlank(message = "Fields are required")
  private String fields;

  private String condition;
}
