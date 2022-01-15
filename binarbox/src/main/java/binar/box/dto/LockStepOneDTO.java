package binar.box.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class LockStepOneDTO {
    private Long id;

    @NotNull
    private Long lockTemplate;

    @NotNull
    private String message;

    @NotNull
    private Boolean privateLock;
    
    @NotNull
    private String userId;

    //TODO:
    // fontSize
    // font
    // color
    // add all to DB
}
