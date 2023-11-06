package pan.artem.tinkoff.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "role")
    private List<UserEntity> users;
}
