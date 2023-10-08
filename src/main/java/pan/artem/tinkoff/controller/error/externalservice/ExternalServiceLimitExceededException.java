package pan.artem.tinkoff.controller.error.externalservice;

public class ExternalServiceLimitExceededException extends ExternalServiceException {

    public ExternalServiceLimitExceededException(String message) {
        super(message);
    }
}
