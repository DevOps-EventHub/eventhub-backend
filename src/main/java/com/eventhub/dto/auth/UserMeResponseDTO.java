package com.eventhub.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.Set;

@Schema(description = "Dados do usuÃ¡rio autenticado")
public record UserMeResponseDTO(
        Long id,
        String name,
        String email,
        Set<String> roles,
        OffsetDateTime createdAt
) {}
