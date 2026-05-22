package com.eventhub.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

@Schema(description = "Dados para criaÃ§Ã£o/atualizaÃ§Ã£o de evento")
public record EventRequestDTO(
        @NotBlank @Size(max = 180) String title,
        @Size(max = 5000) String description,
        @NotNull Long categoryId,
        @NotBlank @Size(max = 120) String location,
        @NotNull @Future OffsetDateTime startAt,
        @NotNull @Future OffsetDateTime endAt,
        @NotNull @Positive Integer capacity,
        @NotBlank String status
) {}
