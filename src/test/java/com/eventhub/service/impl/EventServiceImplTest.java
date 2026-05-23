package com.eventhub.service.impl;

import com.eventhub.dto.event.EventRequestDTO;
import com.eventhub.entity.Category;
import com.eventhub.entity.Event;
import com.eventhub.entity.enums.EventStatus;
import com.eventhub.exception.ResourceNotFoundException;
import com.eventhub.repository.CategoryRepository;
import com.eventhub.repository.EventRepository;
import com.eventhub.repository.RegistrationRepository;
import com.eventhub.repository.SavedEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SavedEventRepository savedEventRepository;
    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private Category category;
    private Event event;
    private EventRequestDTO request;

    @BeforeEach
    void setUp() {
        category = Category.builder().id(1L).name("Tech").description("Tech events").build();
        event = Event.builder()
                .id(10L)
                .title("Summit")
                .description("Desc")
                .category(category)
                .location("Sao Paulo")
                .startAt(OffsetDateTime.now().plusDays(2))
                .endAt(OffsetDateTime.now().plusDays(2).plusHours(4))
                .capacity(100)
                .status(EventStatus.PUBLICADO)
                .build();
        request = new EventRequestDTO(
                "Updated Summit",
                "Updated Desc",
                1L,
                "Campinas",
                OffsetDateTime.now().plusDays(3),
                OffsetDateTime.now().plusDays(3).plusHours(2),
                200,
                "PUBLICADO"
        );
    }

    @Test
    void deleteShouldRemoveRelationsAndEvent() {
        when(eventRepository.existsById(10L)).thenReturn(true);

        eventService.delete(10L);

        verify(savedEventRepository, times(1)).deleteAllByEventId(10L);
        verify(registrationRepository, times(1)).deleteAllByEventId(10L);
        verify(eventRepository, times(1)).deleteById(10L);
    }

    @Test
    void deleteShouldThrowWhenEventNotFound() {
        when(eventRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> eventService.delete(999L));
    }

    @Test
    void createShouldPersistEvent() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event saved = invocation.getArgument(0);
            saved.setId(22L);
            return saved;
        });

        var response = eventService.create(request);

        assertNotNull(response);
        assertEquals("Updated Summit", response.title());
        assertEquals("Tech", response.category());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void updateShouldPersistChanges() {
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = eventService.update(10L, request);

        assertEquals("Updated Summit", response.title());
        assertEquals("Campinas", response.location());
        assertEquals(200, response.capacity());
    }

    @Test
    void findByIdShouldReturnEvent() {
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));

        var response = eventService.findById(10L);

        assertEquals(10L, response.id());
        assertEquals("Summit", response.title());
    }

    @Test
    void findAllShouldReturnPage() {
        Page<Event> page = new PageImpl<>(List.of(event));
        when(eventRepository.search(eq(""), eq(""), eq(null), eq(""), any(PageRequest.class))).thenReturn(page);

        var result = eventService.findAll(null, null, null, null, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Summit", result.getContent().get(0).title());
    }
}
