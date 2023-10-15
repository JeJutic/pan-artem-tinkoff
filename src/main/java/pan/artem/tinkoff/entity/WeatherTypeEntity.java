package pan.artem.tinkoff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "weather_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WeatherTypeEntity {

    @Id
    private Long id;
    private String description;

    @OneToMany
    @JoinTable(name = "weather",
            joinColumns = @JoinColumn(name = "weather_type_id"),
            inverseJoinColumns = @JoinColumn(name = "city_id"))
    private List<CityEntity> cities;

}
