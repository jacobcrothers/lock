package binar.box.converter;

import binar.box.domain.Video;
import binar.box.dto.VideoDTO;
import org.springframework.stereotype.Component;

@Component
public class VideoConverter {

    public VideoDTO toDTO(Video video) {
        VideoDTO dto = new VideoDTO();
        dto.setId(video.getId());
        dto.setName(video.getName());
        dto.setStreamingURL(video.getStreamingURL());
        dto.setThumbnailURL(video.getThumbnailURL());
        dto.setUploadTime(video.getUploadTime());
        dto.setName(video.getName());
        return dto;
    }
}
