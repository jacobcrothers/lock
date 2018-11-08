package binar.box.util.Exceptions.base;

public class BaseLocalizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String messageKey;
    private Object[] messageArguments;

    public BaseLocalizedException(String message) {
        super(message);
    }

    public BaseLocalizedException(String messageKey, Object... messageArguments) {
        super();
        this.messageKey = messageKey;
        this.messageArguments = messageArguments;
    }

    public BaseLocalizedException(String message, String messageKey, Object... messageArguments) {
        super(message);
        this.messageKey = messageKey;
        this.messageArguments = messageArguments;
    }
}
