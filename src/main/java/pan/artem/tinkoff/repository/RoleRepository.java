package pan.artem.tinkoff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pan.artem.tinkoff.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
