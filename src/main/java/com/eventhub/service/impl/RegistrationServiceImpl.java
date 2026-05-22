package com.eventhub.service.impl;

import com.eventhub.dto.registration.RegistrationResponseDTO;
import com.eventhub.entity.Event;
import com.eventhub.entity.Registration;
import com.eventhub.entity.User;
import com.eventhub.entity.enums.RegistrationStatus;
import com.eventhub.exception.BusinessException;
import com.eventhub.exception.ResourceNotFoundException;
import static com.eventhub.exception.ErrorMessages.*;
import com.eventhub.repository.EventRepository;
import com.eventhub.repository.RegistrationRepository;
import com.eventhub.repository.UserRepository;
import com.eventhub.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public RegistrationResponseDTO register(Long eventId) {
        User user = getAuthenticatedUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(EVENT_NOT_FOUND));

        if (registrationRepository.findByUserIdAndEventId(user.getId(), eventId).isPresent()) {
            throw new BusinessException(USER_ALREADY_REGISTERED_IN_EVENT);
        }

        Registration registration = Registration.builder()
                .user(user)
                .event(event)
                .status(RegistrationStatus.CONFIRMADO)
                .registeredAt(OffsetDateTime.now())
                .build();

        return toResponse(registrationRepository.save(registration));
    }

    @Override
    public void cancel(Long eventId) {
        User user = getAuthenticatedUser();
        Registration registration = registrationRepository.findByUserIdAndEventId(user.getId(), eventId)
                .orElseThrow(() -> new ResourceNotFoundException(REGISTRATION_NOT_FOUND));
        registrationRepository.delete(registration);
    }

    @Override
    public List<RegistrationResponseDTO> findMyRegistrations() {
        User user = getAuthenticatedUser();
        return registrationRepository.findAllByUserOrderByRegisteredAtDesc(user).stream().map(this::toResponse).toList();
    }

    @Override
    public List<RegistrationResponseDTO> findRegistrationsByEventId(Long eventId) {
        return registrationRepository.findAllByEventIdOrderByRegisteredAtDesc(eventId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private RegistrationResponseDTO toResponse(Registration r) {
        return new RegistrationResponseDTO(r.getEvent().getId(), r.getEvent().getTitle(), r.getStatus().name(), r.getRegisteredAt());
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new BusinessException(UNAUTHENTICATED_USER);
        }
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }
}

