package binar.box.util.Exceptions;

import binar.box.util.Exceptions.base.BaseLocalizedException;
import org.springframework.validation.BindingResult;

public class FieldsException extends BaseLocalizedException {

    private static final long serialVersionUID = 54447954588764268L;

    private BindingResult result;

    public FieldsException(String message, BindingResult result) {
        super(message);
        this.result = result;
    }

    public FieldsException(String message, String messageKey, BindingResult result, Object... messageArguments) {
        super(message, messageKey, messageArguments);
        this.result = result;
    }

    public BindingResult getResult() {
        return result;
    }
}