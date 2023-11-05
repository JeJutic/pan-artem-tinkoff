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
public class WeatherType {

    @Id
    private Long id;
    private String description;

    @OneToMany(mappedBy = "weatherType")
    private List<Weather> weathers;

}
