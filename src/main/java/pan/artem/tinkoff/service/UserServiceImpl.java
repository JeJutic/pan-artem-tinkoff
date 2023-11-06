package pan.artem.tinkoff.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pan.artem.tinkoff.entity.UserEntity;
import pan.artem.tinkoff.exception.UserAlreadyExistsException;
import pan.artem.tinkoff.properties.AppProperties;
import pan.artem.tinkoff.repository.RoleRepository;
import pan.artem.tinkoff.repository.UserRepository;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties properties;

    @PostConstruct
    public void addAdmin() {
        try {
            registerUser("admin", properties.getAdminPassword(), "ADMIN");
        } catch (DataIntegrityViolationException ignored) { // admin user might be already created
        }
    }

    @Override
    public void registerUser(String name, String password, String role) {
        UserEntity user = new UserEntity();
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(roleRepository.findByName(role));
        try {
            userRepository.save(user);
        } catch (ValidationException e) {
            throw new UserAlreadyExistsException(
                    "User with nickname " + name + " already exists"
            );
        }
    }
}
