package pan.artem.tinkoff.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistsException extends AuthenticationException {

    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
