package kg.parser.blastmaker.xlms.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ex {

    private String name;
    private String driver;
    private String typeofWork;
    private double waste;
    private double wastePerKM;
    private double weightFact;
    private double weightNorm;
    private double distance;
    private List<TruckForEx> truks;
    private Timestamp timePlan;
    private Timestamp timeFact;
}
