package com.umutbasket.berkyazici.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Video> videos = new ArrayList<>();

    // Bu metotlar, iki entity arasındaki ilişkiyi senkronize tutmak için önemlidir.
    public void addVideo(Video video) {
        videos.add(video);
        video.setCategory(this);
    }

    public void removeVideo(Video video) {
        videos.remove(video);
        video.setCategory(null);
    }
}