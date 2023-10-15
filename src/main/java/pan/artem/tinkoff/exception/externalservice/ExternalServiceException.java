package pan.artem.tinkoff.exception.externalservice;

import pan.artem.tinkoff.exception.MyAppException;

public class ExternalServiceException extends MyAppException {

    public ExternalServiceException() {
    }

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
