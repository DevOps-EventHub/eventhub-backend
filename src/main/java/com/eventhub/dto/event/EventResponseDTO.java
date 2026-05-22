package com.eventhub.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Resposta de evento")
public record EventResponseDTO(
        Long id,
        String title,
        String description,
        String category,
        String location,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        Integer capacity,
        String status,
        String imageUrl
) {}
