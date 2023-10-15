package pan.artem.tinkoff.exception;

public abstract class MyAppException extends RuntimeException {

    public MyAppException() {
    }

    public MyAppException(String message) {
        super(message);
    }

    public MyAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyAppException(Throwable cause) {
        super(cause);
    }
}
