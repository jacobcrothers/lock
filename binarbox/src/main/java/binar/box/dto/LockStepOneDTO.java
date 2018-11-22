package binar.box.dto;

import lombok.Data;

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
}
