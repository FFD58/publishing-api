package ru.fafurin.publishing.service;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public interface FileService {
    String storeFile(MultipartFile file, LocalDateTime createdAt);
}
