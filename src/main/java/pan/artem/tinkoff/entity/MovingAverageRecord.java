package pan.artem.tinkoff.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "moving_average")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MovingAverageRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int temperature;
}
