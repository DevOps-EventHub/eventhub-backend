package com.eventhub.service;

import com.eventhub.dto.registration.RegistrationResponseDTO;

import java.util.List;

public interface RegistrationService {
    RegistrationResponseDTO register(Long eventId);
    void cancel(Long eventId);
    List<RegistrationResponseDTO> findMyRegistrations();
    List<RegistrationResponseDTO> findRegistrationsByEventId(Long eventId);
}
