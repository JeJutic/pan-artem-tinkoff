package pan.artem.tinkoff.service;

public interface AppAuthenticationService {

    boolean login(String username, String password);

    boolean register(String username, String password);
}
