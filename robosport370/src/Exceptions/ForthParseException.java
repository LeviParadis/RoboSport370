package Exceptions;

public class ForthParseException extends Exception {

    public ForthParseException() {
       super();
    }

    public ForthParseException(String message) {
        super(message);
    }

    public ForthParseException(Throwable cause) {
        super(cause);
    }

    public ForthParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForthParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
