package com.example.mcs.service.serviceImpl;

import com.example.mcs.exception.ConversionException;
import com.example.mcs.service.MinioService;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Override
    public void uploadFile(String path, byte[] data) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(data);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .stream(stream, data.length, -1)
                            .build()
            );
        } catch (Exception e) {
            throw new ConversionException("Failed to upload file", e);
        }
    }

    @Override
    public byte[] downloadFile(String path) {
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
            return response.readAllBytes();
        } catch (Exception e) {
            throw new ConversionException("Failed to download file", e);
        }
    }
}