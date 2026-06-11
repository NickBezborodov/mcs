package com.example.mcs.service.serviceImpl;

import com.example.mcs.model.Inbox;
import com.example.mcs.repository.InboxRepository;
import com.example.mcs.service.InboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InboxServiceImpl implements InboxService {

    private final InboxRepository inboxRepository;

    @Override
    public boolean isProcessed(String messageKey) {
        return inboxRepository.existsByMessageKey(messageKey);
    }

    @Override
    @Transactional
    public void markProcessed(String messageKey) {
        Inbox inbox = Inbox.builder()
                .messageKey(messageKey)
                .processedAt(LocalDateTime.now())
                .build();
        inboxRepository.save(inbox);
    }
}
