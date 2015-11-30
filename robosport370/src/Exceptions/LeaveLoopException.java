package Exceptions;

public class LeaveLoopException extends Exception {

    /**
     * this exception is thrown when the forth interpreter encounters the leave
     * word It should be caught in a loop handler function
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
