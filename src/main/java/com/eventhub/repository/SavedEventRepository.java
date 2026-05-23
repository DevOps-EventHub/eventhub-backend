package com.eventhub.repository;

import com.eventhub.entity.SavedEvent;
import com.eventhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedEventRepository extends JpaRepository<SavedEvent, Long> {
    Optional<SavedEvent> findByUserIdAndEventId(Long userId, Long eventId);
    List<SavedEvent> findAllByUserOrderBySavedAtDesc(User user);
    List<SavedEvent> findAllByEventIdOrderBySavedAtDesc(Long eventId);
    void deleteAllByEventId(Long eventId);
}
