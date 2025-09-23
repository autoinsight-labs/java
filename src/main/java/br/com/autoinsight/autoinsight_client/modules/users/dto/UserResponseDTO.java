package br.com.autoinsight.autoinsight_client.modules.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados de resposta de um usuário do sistema")
public class UserResponseDTO {
  @Schema(description = "ID único do usuário", example = "cm987654321098765432109876")
  private String id;

  @Schema(description = "Nome completo do usuário", example = "João Silva Santos")
  private String name;

  @Schema(description = "Email do usuário", example = "joao.silva@empresa.com")
  private String email;
}