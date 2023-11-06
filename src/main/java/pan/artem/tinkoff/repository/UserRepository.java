package pan.artem.tinkoff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pan.artem.tinkoff.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByName(String name);
}
