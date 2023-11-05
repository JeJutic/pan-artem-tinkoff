package pan.artem.tinkoff.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pan.artem.tinkoff.entity.City;

@Repository
public interface CityRepositoryJPA extends JpaRepository<City, Long> {

    @Query("SELECT c FROM City c LEFT JOIN FETCH c.weathers WHERE c.name = :cityName")
    City getByNameWithWeatherFetching(@Param("cityName") String name);

}
