package com.eventhub.service.impl;

import com.eventhub.dto.saved.SavedEventParticipantResponseDTO;
import com.eventhub.dto.saved.SavedEventResponseDTO;
import com.eventhub.entity.Event;
import com.eventhub.entity.SavedEvent;
import com.eventhub.entity.User;
import com.eventhub.exception.BusinessException;
import com.eventhub.exception.ResourceNotFoundException;
import static com.eventhub.exception.ErrorMessages.*;
import com.eventhub.repository.EventRepository;
import com.eventhub.repository.SavedEventRepository;
import com.eventhub.repository.UserRepository;
import com.eventhub.service.SavedEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedEventServiceImpl implements SavedEventService {

    private final SavedEventRepository savedEventRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public void saveEvent(Long eventId) {
        User user = getAuthenticatedUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(EVENT_NOT_FOUND));

        if (savedEventRepository.findByUserIdAndEventId(user.getId(), eventId).isPresent()) {
            throw new BusinessException(EVENT_ALREADY_SAVED);
        }

        SavedEvent savedEvent = SavedEvent.builder()
                .user(user)
                .event(event)
                .savedAt(OffsetDateTime.now())
                .build();

        savedEventRepository.save(savedEvent);
    }

    @Override
    public void removeSavedEvent(Long eventId) {
        User user = getAuthenticatedUser();
        SavedEvent savedEvent = savedEventRepository.findByUserIdAndEventId(user.getId(), eventId)
                .orElseThrow(() -> new ResourceNotFoundException(SAVED_EVENT_NOT_FOUND));
        savedEventRepository.delete(savedEvent);
    }

    @Override
    public List<SavedEventResponseDTO> findMySavedEvents() {
        User user = getAuthenticatedUser();
        return savedEventRepository.findAllByUserOrderBySavedAtDesc(user)
                .stream()
                .map(s -> new SavedEventResponseDTO(s.getEvent().getId(), s.getEvent().getTitle(), s.getEvent().getDescription(), s.getEvent().getCategory().getName(), s.getEvent().getLocation(), s.getEvent().getStartAt(), s.getSavedAt(), s.getEvent().getImageUrl()))
                .toList();
    }

    @Override
    public List<SavedEventParticipantResponseDTO> findParticipantsByEventId(Long eventId) {
        return savedEventRepository.findAllByEventIdOrderBySavedAtDesc(eventId)
                .stream()
                .map(s -> new SavedEventParticipantResponseDTO(s.getUser().getId(), s.getUser().getName(), s.getUser().getEmail(), s.getSavedAt()))
                .toList();
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

