package binar.box.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class LockStepOneDTO {
    //TODO: ADD MULTIPART FILE FOR PROCESSED LOCK
    MultipartFile lockImageWithText;

    private Long id;

    @NotNull
    private Long lockTemplate;

    @NotNull
    private String message;

    @NotNull
    private Boolean privateLock;
}
