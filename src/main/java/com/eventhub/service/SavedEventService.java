package com.eventhub.service;

import com.eventhub.dto.saved.SavedEventParticipantResponseDTO;
import com.eventhub.dto.saved.SavedEventResponseDTO;

import java.util.List;

public interface SavedEventService {
    void saveEvent(Long eventId);
    void removeSavedEvent(Long eventId);
    List<SavedEventResponseDTO> findMySavedEvents();
    List<SavedEventParticipantResponseDTO> findParticipantsByEventId(Long eventId);
}
