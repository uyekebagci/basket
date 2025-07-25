package com.umutbasket.berkyazici.mapper;

import com.umutbasket.berkyazici.dto.VideoResponseDTO;
import com.umutbasket.berkyazici.entity.Video;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideoMapper {

    public VideoResponseDTO toVideoResponseDTO(Video video) {
        return VideoResponseDTO.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .durationInSeconds(video.getDurationInSeconds())
                .uploadDate(video.getUploadDate())
                .categoryName(video.getCategory() != null ? video.getCategory().getName() : null)
                .build();
    }

    public List<VideoResponseDTO> toVideoResponseDTOList(List<Video> videos) {
        return videos.stream()
                .map(this::toVideoResponseDTO)
                .collect(Collectors.toList());
    }
}