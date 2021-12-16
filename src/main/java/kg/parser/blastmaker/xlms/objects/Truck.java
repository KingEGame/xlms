package kg.parser.blastmaker.xlms.objects;


import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "truck")
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String name;

    private double carrying_capacity;

    private double volume;

    private double max_speed;

    private double carring_capasity_opt;

    private double optimal_speed;

    private double waste_gas_for_reise;

    private double normal_weight;

    private double specific_waste_gas;
}
