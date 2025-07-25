package com.umutbasket.berkyazici.service.storage;

import com.umutbasket.berkyazici.exception.StorageException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        // Yolu hemen mutlak (absolute) hale getiriyoruz.
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            // Servis ilk yaratıldığında yükleme klasörünü oluşturuyoruz.
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    public String store(MultipartFile file) {
        // Dosya adını güvenlik için temizliyoruz.
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            // ../ gibi path traversal saldırılarını önlemek için ekstra kontrol
            if (filename.contains("..")) {
                throw new StorageException("Cannot store file with relative path outside current directory " + filename);
            }

            // Dosya adına benzersiz bir kimlik ekleyerek çakışmaları önlüyoruz
            String fileExtension = "";
            int lastDotIndex = filename.lastIndexOf('.');
            if (lastDotIndex >= 0) {
                fileExtension = filename.substring(lastDotIndex);
            }
            String newFilename = UUID.randomUUID().toString() + fileExtension;


            // Dosyayı hedef konuma kopyalıyoruz.
            Path destinationFile = this.rootLocation.resolve(newFilename);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                return newFilename;
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }
}