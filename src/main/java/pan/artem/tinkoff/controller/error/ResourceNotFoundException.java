package pan.artem.tinkoff.controller.error;

public class ResourceNotFoundException extends MyAppException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
