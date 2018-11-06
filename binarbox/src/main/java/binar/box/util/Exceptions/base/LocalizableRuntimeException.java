package binar.box.util.Exceptions.base;

public class LocalizableRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String messageKey;
    private Object[] messageArguments;

    public LocalizableRuntimeException(String message) {
        super(message);
    }

    public LocalizableRuntimeException(String messageKey, Object... messageArguments) {
        super();
        this.messageKey = messageKey;
        this.messageArguments = messageArguments;
    }

    public LocalizableRuntimeException(String message, String messageKey, Object... messageArguments) {
        super(message);
        this.messageKey = messageKey;
        this.messageArguments = messageArguments;
    }
}
