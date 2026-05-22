package com.eventhub.repository;

import com.eventhub.entity.Registration;
import com.eventhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);
    List<Registration> findAllByUserOrderByRegisteredAtDesc(User user);
    List<Registration> findAllByEventIdOrderByRegisteredAtDesc(Long eventId);
}
