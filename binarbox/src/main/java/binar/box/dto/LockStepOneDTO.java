package binar.box.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class LockStepOneDTO {

    private Long id;

    @NotNull
    private Long lockTemplate;

    @NotNull
    private String message;

    @NotNull
    private Boolean privateLock;
}
