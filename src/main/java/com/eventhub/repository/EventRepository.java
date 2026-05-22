package com.eventhub.repository;

import com.eventhub.entity.Event;
import com.eventhub.entity.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
            select e from Event e
            where lower(e.title) like concat('%', :title, '%')
              and (:category = '' or lower(e.category.name) = :category)
              and (:status is null or e.status = :status)
              and lower(e.location) like concat('%', :location, '%')
            """)
    Page<Event> search(
            @Param("title") String title,
            @Param("category") String category,
            @Param("status") EventStatus status,
            @Param("location") String location,
            Pageable pageable
    );
}
