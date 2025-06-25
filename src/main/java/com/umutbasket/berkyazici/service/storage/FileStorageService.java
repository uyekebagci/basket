package com.umutbasket.berkyazici.service.storage;

import com.umutbasket.berkyazici.exception.StorageException;
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

    // application.properties'deki 'file.upload-dir' değerini enjekte ediyoruz.
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
        try {
            // Servis ilk yaratıldığında yükleme klasörünü oluşturuyoruz.
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    /**
     * Gelen dosyayı sunucuda saklar ve benzersiz dosya adını döner.
     * @param file Yüklenen MultipartFile
     * @return Saklanan dosyanın benzersiz adı (örn: "random-uuid-original-name.mp4")
     */
    public String store(MultipartFile file) {
        // Dosya adından ../ gibi path traversal saldırılarını önlemek için temizlik yapıyoruz.
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + originalFilename);
            }
            if (originalFilename.contains("..")) {
                throw new StorageException("Cannot store file with relative path outside current directory " + originalFilename);
            }

            // Dosya adının benzersiz olmasını sağlamak için başına bir UUID ekliyoruz.
            String fileExtension = "";
            try {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            } catch (Exception e) {
                // Dosya uzantısı yoksa sorun değil.
            }
            String newFilename = UUID.randomUUID().toString() + fileExtension;


            try (InputStream inputStream = file.getInputStream()) {
                Path destinationFile = this.rootLocation.resolve(Paths.get(newFilename))
                        .normalize().toAbsolutePath();

                // Klasörün hala var olduğundan emin oluyoruz.
                if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                    throw new StorageException("Cannot store file outside current directory " + originalFilename);
                }

                // Dosyayı hedef konuma kopyalıyoruz, eğer varsa üzerine yazıyoruz.
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

                return newFilename;
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + originalFilename, e);
        }
    }
}