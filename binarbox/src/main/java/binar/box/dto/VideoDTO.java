package binar.box.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class VideoDTO {
    private Long id;
    private String streamingURL;
    private Date uploadExpirationDate;
    private boolean uploadedSourceFile;
    private Date uploadTime;
    private String thumbnailURL;
    private String name;
}
