package pan.artem.tinkoff.exception.externalservice;

import lombok.Getter;

@Getter
public class ExternalServiceClientErrorException extends ExternalServiceException {

    private final int statusCode;
    private final int errorCode;

    public ExternalServiceClientErrorException(String message, int statusCode, int errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
}
