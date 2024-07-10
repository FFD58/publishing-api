package ru.fafurin.publishing.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fafurin.publishing.service.FileService;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class FileServiceImpl implements FileService {

    private static final String UPLOAD_DIR = "files/";

    @Override
    public String storeFile(MultipartFile file, LocalDateTime createdAt) {
        String storageFileName = createdAt.getNano() + "_" + file.getOriginalFilename();
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(
                        inputStream,
                        Paths.get(UPLOAD_DIR + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return storageFileName;
    }
}
