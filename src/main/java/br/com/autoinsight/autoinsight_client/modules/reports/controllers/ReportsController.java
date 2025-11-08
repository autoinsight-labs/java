package br.com.autoinsight.autoinsight_client.modules.reports.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.autoinsight.autoinsight_client.modules.reports.dto.RelationalToJsonRequestDTO;
import br.com.autoinsight.autoinsight_client.modules.reports.dto.ReportsResponseDTO;
import br.com.autoinsight.autoinsight_client.modules.reports.dto.ValidatePasswordRequestDTO;
import br.com.autoinsight.autoinsight_client.modules.reports.services.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Relatórios", description = "Endpoints para execução de procedures e funções do banco de dados")
@SecurityRequirement(name = "bearerAuth")
public class ReportsController {

  @Autowired
  private ReportsService reportsService;

  @GetMapping("/yards-report-json")
  @Operation(summary = "Relatório de pátios em JSON", description = "Executa a procedure PKG_REPORTS.sp_yards_report_json que retorna um relatório de pátios com veículos em formato JSON")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportsResponseDTO.class))),
      @ApiResponse(responseCode = "500", description = "Erro ao executar a procedure", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<ReportsResponseDTO> getYardsReportJson() {
    try {
      String result = reportsService.executeYardsReportJson();
      return ResponseEntity.ok(new ReportsResponseDTO(result, null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ReportsResponseDTO(null, e.getMessage()));
    }
  }

  @GetMapping("/grouped-report-tabular")
  @Operation(summary = "Relatório agrupado tabular", description = "Executa a procedure PKG_REPORTS.sp_grouped_report_tabular que retorna um relatório agrupado de veículos por pátio em formato tabular")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso", content = @Content(mediaType = "text/plain")),
      @ApiResponse(responseCode = "500", description = "Erro ao executar a procedure", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<ReportsResponseDTO> getGroupedReportTabular() {
    try {
      String result = reportsService.executeGroupedReportTabular();
      return ResponseEntity.ok(new ReportsResponseDTO(result, null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ReportsResponseDTO(null, e.getMessage()));
    }
  }

  @PostMapping("/relational-to-json")
  @Operation(summary = "Converter dados relacionais para JSON", description = "Executa a função PKG_UTILS.fn_relational_to_json que converte dados de uma tabela para formato JSON")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Conversão realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportsResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro ao executar a função", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<ReportsResponseDTO> relationalToJson(@Valid @RequestBody RelationalToJsonRequestDTO request) {
    try {
      String result = reportsService.executeRelationalToJson(
          request.getTable(),
          request.getFields(),
          request.getCondition());
      return ResponseEntity.ok(new ReportsResponseDTO(result, null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ReportsResponseDTO(null, e.getMessage()));
    }
  }

  @PostMapping("/validate-password")
  @Operation(summary = "Validar senha", description = "Executa a função PKG_UTILS.fn_validate_password que valida se uma senha atende aos requisitos de segurança")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Validação realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportsResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro ao executar a função", content = @Content),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content)
  })
  public ResponseEntity<ReportsResponseDTO> validatePassword(@Valid @RequestBody ValidatePasswordRequestDTO request) {
    try {
      String result = reportsService.executeValidatePassword(request.getPassword());
      return ResponseEntity.ok(new ReportsResponseDTO(result, null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ReportsResponseDTO(null, e.getMessage()));
    }
  }
}
