package com.eventhub.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticaÃ§Ã£o com JWT")
public record AuthResponseDTO(
        @Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,
        @Schema(example = "Bearer")
        String tokenType,
        UserMeResponseDTO user
) {}
