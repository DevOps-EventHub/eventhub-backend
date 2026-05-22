package com.eventhub.dto.saved;

import java.time.OffsetDateTime;

public record SavedEventParticipantResponseDTO(
        Long userId,
        String name,
        String email,
        OffsetDateTime savedAt
) {}
