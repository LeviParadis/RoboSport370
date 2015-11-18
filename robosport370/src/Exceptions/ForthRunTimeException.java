package Exceptions;

public class ForthRunTimeException extends Exception {
    /**
     * this exception is thrown when the forth interpreter encounters an error when it is trying to run a program
     */
    
    
    private static final long serialVersionUID = -8050699301872910522L;

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
