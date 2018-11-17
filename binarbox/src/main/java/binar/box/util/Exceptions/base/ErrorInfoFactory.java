package binar.box.util.Exceptions.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Component
public class ErrorInfoFactory {

    private MessageSource messageSource;

    @Autowired
    public ErrorInfoFactory(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

	public static ErrorInfo buildBadRequestErrorInfo(String message, String messageKey, BindingResult bindingResult) {
		List<FieldErrorInfo> fieldErrors = new ArrayList<>();
		List<FieldError> errors = bindingResult.getFieldErrors();
		for (FieldError error : errors) {
			fieldErrors.add(new FieldErrorInfo(error.getDefaultMessage(), error.getField(), messageKey + "." + error.getField() + "." + error.getCode(),
					error.getRejectedValue()));
		}
		return ErrorInfo.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message(message)
            .messageKey(messageKey)
            .fieldErrors(fieldErrors)
            .build();
	}
}
