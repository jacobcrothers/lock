package binar.box.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class VideoUploadDTO {
    private Date expirationTime;
    private Long videoId;
    private String videoUploadURL;
    private String thumbnailURL;
}
