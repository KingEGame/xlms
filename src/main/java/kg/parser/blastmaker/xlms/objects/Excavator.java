package kg.parser.blastmaker.xlms.objects;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "excavator")
public class Excavator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String name;

    private double carrying_capacity;

    private double volume;

    private double max_speed;

    private double carring_capasity_opt;

}
