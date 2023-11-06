package pan.artem.tinkoff.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pan.artem.tinkoff.entity.WeatherType;

@Repository
public interface WeatherTypeRepositoryJPA extends JpaRepository<WeatherType, Long> {

    WeatherType getByDescription(String description);
}
