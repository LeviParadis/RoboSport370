package Exceptions;

public class ForthRunTimeException extends Exception {

    private static final long serialVersionUID = 1L;

    public ForthRunTimeException() {
        super();
    }

    public ForthRunTimeException(String message) {
        super(message);
    }

    public ForthRunTimeException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public ForthRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForthRunTimeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
