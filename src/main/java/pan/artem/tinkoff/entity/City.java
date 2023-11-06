package pan.artem.tinkoff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "city")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "city",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<Weather> weathers;

}
