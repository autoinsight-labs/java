package br.com.autoinsight.autoinsight_client.modules.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportsResponseDTO {
  private String result;
  private String error;
}
