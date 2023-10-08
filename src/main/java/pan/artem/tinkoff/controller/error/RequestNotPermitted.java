package pan.artem.tinkoff.controller.error;

public class RequestNotPermitted extends MyAppException {

    public RequestNotPermitted(Throwable cause) {
        super(cause);
    }
}
