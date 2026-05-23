package com.eventhub.service.impl;

import com.eventhub.dto.event.EventRequestDTO;
import com.eventhub.dto.event.EventResponseDTO;
import com.eventhub.entity.Category;
import com.eventhub.entity.Event;
import com.eventhub.entity.enums.EventStatus;
import com.eventhub.exception.ResourceNotFoundException;
import static com.eventhub.exception.ErrorMessages.*;
import com.eventhub.repository.CategoryRepository;
import com.eventhub.repository.EventRepository;
import com.eventhub.repository.RegistrationRepository;
import com.eventhub.repository.SavedEventRepository;
import com.eventhub.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final SavedEventRepository savedEventRepository;
    private final RegistrationRepository registrationRepository;

    @Override
    public Page<EventResponseDTO> findAll(String title, String category, String status, String location, Pageable pageable) {
        EventStatus eventStatus = parseStatus(status);
        String normalizedTitle = normalizeFilterOrEmpty(title);
        String normalizedCategory = normalizeFilterOrEmpty(category);
        String normalizedLocation = normalizeFilterOrEmpty(location);
        return eventRepository.search(normalizedTitle, normalizedCategory, eventStatus, normalizedLocation, pageable)
                .map(this::toResponse);
    }

    @Override
    public EventResponseDTO findById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EVENT_NOT_FOUND));
        return toResponse(event);
    }

    @Override
    public EventResponseDTO create(EventRequestDTO request) {
        return toResponse(eventRepository.save(fromRequest(null, request)));
    }

    @Override
    public EventResponseDTO update(Long id, EventRequestDTO request) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EVENT_NOT_FOUND));
        return toResponse(eventRepository.save(fromRequest(existing, request)));
    }

    @Override
    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException(EVENT_NOT_FOUND);
        }
        savedEventRepository.deleteAllByEventId(id);
        registrationRepository.deleteAllByEventId(id);
        eventRepository.deleteById(id);
    }

    private Event fromRequest(Event existing, EventRequestDTO request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND));

        Event event = existing == null ? new Event() : existing;
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setCategory(category);
        event.setLocation(request.location());
        event.setStartAt(request.startAt());
        event.setEndAt(request.endAt());
        event.setCapacity(request.capacity());
        if (existing == null) {
            event.setImageUrl(null);
        }
        event.setStatus(parseStatus(request.status()));
        return event;
    }

    private EventStatus parseStatus(String status) {
        if (status == null) {
            return null;
        }
        try {
            return EventStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new com.eventhub.exception.BusinessException(INVALID_EVENT_STATUS);
        }
    }

    private String normalizeFilterOrEmpty(String value) {
        if (value == null) return "";
        return value.trim().toLowerCase();
    }

    private EventResponseDTO toResponse(Event e) {
        return new EventResponseDTO(e.getId(), e.getTitle(), e.getDescription(), e.getCategory().getName(), e.getLocation(), e.getStartAt(), e.getEndAt(), e.getCapacity(), e.getStatus().name(), e.getImageUrl());
    }
}

