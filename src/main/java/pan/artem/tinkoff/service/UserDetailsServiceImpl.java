package pan.artem.tinkoff.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pan.artem.tinkoff.entity.UserEntity;
import pan.artem.tinkoff.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(
                    "no user with username " + username + " found"
            );
        }
        var role = new SimpleGrantedAuthority("ROLE_" + user.getRole().getName());

        return new User(username, user.getPassword(), List.of(role));
    }
}
