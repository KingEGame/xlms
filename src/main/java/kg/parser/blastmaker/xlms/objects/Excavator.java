package kg.parser.blastmaker.xlms.objects;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ex")
public class Excavator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private double carrying_capacity_max;

    private double carrying_capacity_norm;

    private double volume;

    private double max_speed;

    private double carring_capasity_opt;

}
