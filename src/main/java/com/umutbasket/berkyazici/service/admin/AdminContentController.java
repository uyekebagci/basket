package com.umutbasket.berkyazici.controller.admin;

import com.umutbasket.berkyazici.entity.Category;
import com.umutbasket.berkyazici.entity.Video;
import com.umutbasket.berkyazici.exception.ResourceNotFoundException; // Bu sınıfı birazdan oluşturacağız
import com.umutbasket.berkyazici.repository.CategoryRepository;
import com.umutbasket.berkyazici.repository.VideoRepository;
import com.umutbasket.berkyazici.service.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
public class AdminContentController {

    private final FileStorageService fileStorageService;
    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;

    // Test kolaylığı için Kategori oluşturma endpoint'i
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Map<String, String> payload) {
        String categoryName = payload.get("name");
        Category newCategory = new Category();
        newCategory.setName(categoryName);
        Category savedCategory = categoryRepository.save(newCategory);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @PostMapping("/videos")
    public ResponseEntity<Video> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId) {

        // 1. Kategoriyi bul
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        // 2. Dosyayı diske kaydet
        String filePath = fileStorageService.store(file);

        // 3. Video entity'sini oluştur ve veritabanına kaydet
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setFilePath(filePath);
        video.setCategory(category);
        // video.setDurationInSeconds(); // TODO: Video süresini alma işlemi daha sonra eklenecek.

        Video savedVideo = videoRepository.save(video);

        return new ResponseEntity<>(savedVideo, HttpStatus.CREATED);
    }
}