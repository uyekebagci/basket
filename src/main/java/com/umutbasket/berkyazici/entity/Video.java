package com.umutbasket.berkyazici.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob // Uzun metinler için kullanılır.
    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String filePath; // Videonun saklandığı yol (örn: /uploads/videos/abc.mp4)

    private Integer durationInSeconds; // Videonun saniye cinsinden süresi

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id") // 'videos' tablosunda kategoriye referans veren kolon
    private Category category;

    @PrePersist
    protected void onCreate() {
        this.uploadDate = LocalDateTime.now();
    }
}