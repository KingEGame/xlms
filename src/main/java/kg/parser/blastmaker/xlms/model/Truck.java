package kg.parser.blastmaker.xlms.model;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Truck {

    private  int num;
    private int reice;
    private double speed;
    private int quantity;
    private String name;
    private double distance;

    private double weight_fact;
    private double weight_norm;

    private double specific_waste_with_mass;
    private double specific_waste_without_mass;

    private double waste;
    private double wastePerKM;

    private String model;

    private String ex;

    private int count;

    private double avg_speed;

    private double avg_waste_gas;

    private double waste_gas;

    private String typeOfWork;
}
