package com.eventhub.controller;

import com.eventhub.dto.saved.SavedEventParticipantResponseDTO;
import com.eventhub.dto.saved.SavedEventResponseDTO;
import com.eventhub.service.SavedEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-events")
@RequiredArgsConstructor
public class SavedEventController {

    private final SavedEventService savedEventService;

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> salvarEvento(@PathVariable Long eventId) {
        savedEventService.saveEvent(eventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> removerEventoSalvo(@PathVariable Long eventId) {
        savedEventService.removeSavedEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SavedEventResponseDTO>> listarEventosSalvos() {
        return ResponseEntity.ok(savedEventService.findMySavedEvents());
    }

    @GetMapping("/event/{eventId}/participants")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SavedEventParticipantResponseDTO>> listarParticipantesPorEvento(@PathVariable Long eventId) {
        return ResponseEntity.ok(savedEventService.findParticipantsByEventId(eventId));
    }
}
