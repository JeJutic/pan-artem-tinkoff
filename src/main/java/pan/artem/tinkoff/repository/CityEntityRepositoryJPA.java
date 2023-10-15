package pan.artem.tinkoff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pan.artem.tinkoff.entity.CityEntity;

public interface CityEntityRepositoryJPA extends JpaRepository<CityEntity, Long> {

    CityEntity getByName(String name);
}
