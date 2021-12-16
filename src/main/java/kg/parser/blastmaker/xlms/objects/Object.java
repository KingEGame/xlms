package kg.parser.blastmaker.xlms.objects;

import lombok.*;
import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Object")
public class Object {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private int numSamosval;

    private String nameDriverSamosval;

    private String typeSamosval;

    private int reise;

    private String numEx;

    private String nameEx;

    private Time timeOfComeLoading;

    private Time timeOfBeginLoading;

    private Time timeOfLoading;

    private double distance;

    private int weightFact;

    private double weightNorm;

    private String typeOfWork;

    private String placeToUnloading;

    private Time timeOfBeginUnloading;

    private Time timeOfUnloading;

    private int gasForBeginLoading;

    private int gasForBeginUnloading;

}
