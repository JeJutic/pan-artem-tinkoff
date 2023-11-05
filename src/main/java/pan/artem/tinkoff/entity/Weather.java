package pan.artem.tinkoff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "weather")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int temperature;
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "weather_type_id")
    private WeatherType weatherType;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    public Weather(int temperature, LocalDateTime dateTime, WeatherType weatherType, City city) {
        this.temperature = temperature;
        this.dateTime = dateTime;
        this.weatherType = weatherType;
        this.city = city;
    }
}
