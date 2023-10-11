package pan.artem.tinkoff.exception.externalservice;

public class ExternalServiceLimitExceededException extends ExternalServiceException {

    public ExternalServiceLimitExceededException(String message) {
        super(message);
    }
}
