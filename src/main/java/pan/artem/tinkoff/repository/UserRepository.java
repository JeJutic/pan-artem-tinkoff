package pan.artem.tinkoff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pan.artem.tinkoff.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);
}
