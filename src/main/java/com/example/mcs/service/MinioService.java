package com.example.mcs.service;

public interface MinioService {
    byte[] downloadFile(String path);

    void uploadFile(String path, byte[] data);
}
