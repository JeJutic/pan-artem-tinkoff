package pan.artem.tinkoff.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppAuthenticationServiceImpl implements AppAuthenticationService {

    @Override
    public boolean login(String username, String password) {
//        userDetailsManager.
        return false;
    }

    @Override
    public boolean register(String username, String password) {
        return false;
    }
}
