package pan.artem.tinkoff.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "city")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CityEntity {

    @Id
    private Long id;
    private String name;

    @ManyToOne
    @JoinTable(name = "weather",
            joinColumns = @JoinColumn(name = "city_id"),
            inverseJoinColumns = @JoinColumn(name = "weather_type_id"))
    private WeatherTypeEntity weatherTypeEntity;

}
