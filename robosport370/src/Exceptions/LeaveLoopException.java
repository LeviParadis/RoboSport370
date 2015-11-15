package Exceptions;

public class LeaveLoopException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -3088501869185907133L;

    public LeaveLoopException() {
        super();
    }

    public LeaveLoopException(String message) {
        super(message);
    }

    public LeaveLoopException(Throwable cause) {
        super(cause);
    }

    public LeaveLoopException(String message, Throwable cause) {
        super(message, cause);
    }

    public LeaveLoopException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
