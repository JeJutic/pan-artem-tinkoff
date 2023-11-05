package pan.artem.tinkoff.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pan.artem.tinkoff.entity.Weather;

@Repository
public interface WeatherRepositoryJPA extends JpaRepository<Weather, Long> {
}
