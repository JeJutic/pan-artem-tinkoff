package pan.artem.tinkoff.controller.error.externalservice;

import pan.artem.tinkoff.controller.error.MyAppException;

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
