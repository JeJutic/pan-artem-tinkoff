package pan.artem.tinkoff.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_table")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
