package pan.artem.tinkoff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pan.artem.tinkoff.entity.WeatherTypeEntity;

public interface WeatherTypeEntityRepository extends JpaRepository<WeatherTypeEntity, Long> {

    WeatherTypeEntity getByDescription(String description);
}
