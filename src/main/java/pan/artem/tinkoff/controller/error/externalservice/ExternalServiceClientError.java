package pan.artem.tinkoff.controller.error.externalservice;

import lombok.Getter;

@Getter
public class ExternalServiceClientError extends ExternalServiceException {

    private final int statusCode;
    private final int errorCode;

    public ExternalServiceClientError(String message, int statusCode, int errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
}
