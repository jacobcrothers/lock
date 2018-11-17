package binar.box.util.Exceptions.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfo {
    private int status;
    private String messageKey;
    private String message;
    private List<FieldErrorInfo> fieldErrors;

    private String developerMessage;
    private String developerResource;
}
