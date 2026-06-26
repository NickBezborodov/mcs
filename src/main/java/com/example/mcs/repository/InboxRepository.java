package com.example.mcs.repository;

import com.example.mcs.model.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboxRepository extends JpaRepository<Inbox, Long> {
    boolean existsByMessageKey(String messageKey);
}
