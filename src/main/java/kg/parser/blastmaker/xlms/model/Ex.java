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

    private String type;
    private String driver_name;
    private String typeofWork;
    private double waste;
    private double wastePerKM;
    private double weightFact;
    private double weightNorm;
    private double distance;
    private List<Truck> trucks;
    private Timestamp timePlan;
    private Timestamp timeFact;

    private double speed;
    private double timeInHours;
    private double weight_fact;
    private double weight_fact_avarage;
    private double weight_norm;
    private double weight_norm_avarage;
    private int gas;
    private double gas_avarage;
    private double distance_avarage;
}
