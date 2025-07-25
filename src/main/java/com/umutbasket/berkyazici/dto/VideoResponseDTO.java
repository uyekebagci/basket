package com.umutbasket.berkyazici.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VideoResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Integer durationInSeconds;
    private LocalDateTime uploadDate;
    // Kategori ID'si yerine adını göstermek daha kullanıcı dostu
    private String categoryName;
}