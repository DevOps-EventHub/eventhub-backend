package com.eventhub.controller;

import com.eventhub.dto.registration.RegistrationResponseDTO;
import com.eventhub.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/{eventId}")
    public ResponseEntity<RegistrationResponseDTO> criarInscricao(@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.register(eventId));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> cancelarInscricao(@PathVariable Long eventId) {
        registrationService.cancel(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<RegistrationResponseDTO>> listarMinhasInscricoes() {
        return ResponseEntity.ok(registrationService.findMyRegistrations());
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegistrationResponseDTO>> listarInscricoesPorEvento(@PathVariable Long eventId) {
        return ResponseEntity.ok(registrationService.findRegistrationsByEventId(eventId));
    }
}
