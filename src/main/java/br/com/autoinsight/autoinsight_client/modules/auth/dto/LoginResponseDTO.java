package br.com.autoinsight.autoinsight_client.modules.auth.dto;

import br.com.autoinsight.autoinsight_client.modules.users.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Resposta do login contendo token JWT e dados do usuário")
public class LoginResponseDTO {
  @Schema(description = "Token JWT para autenticação nas próximas requisições", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
  private String token;

  @Schema(description = "Dados completos do usuário autenticado")
  private UserResponseDTO user;
}