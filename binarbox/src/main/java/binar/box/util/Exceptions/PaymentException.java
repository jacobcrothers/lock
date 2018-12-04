package binar.box.util.Exceptions;

import binar.box.util.Exceptions.base.BaseLocalizedException;
import org.springframework.validation.BindingResult;

public class PaymentException extends BaseLocalizedException {

    private static final long serialVersionUID = 54447954588764268L;


    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, String messageKey, Object... messageArguments) {
        super(message, messageKey, messageArguments);
    }

}