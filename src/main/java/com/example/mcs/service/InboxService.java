package com.example.mcs.service;

public interface InboxService {
    boolean isProcessed(String messageKey);
    void markProcessed(String messageKey);
}
