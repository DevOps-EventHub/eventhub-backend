package com.eventhub.dto.saved;

import java.time.OffsetDateTime;

public record SavedEventResponseDTO(
        Long eventId,
        String title,
        String description,
        String category,
        String location,
        OffsetDateTime startAt,
        OffsetDateTime savedAt,
        String imageUrl
) {}
