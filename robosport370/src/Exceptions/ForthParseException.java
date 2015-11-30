package Exceptions;

public class ForthParseException extends Exception {

    /**
     * this exception is thrown when the forth parser can't read the input
     * string into forth logic
     */

    private static final long serialVersionUID = 7693870929853260732L;

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
