package kg.parser.blastmaker.xlms.model;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TruckForEx {

    private  int num;
    private int reice;
    private double speed;
    private int quantity;
    private String name;
    private double distance;

    private double weight_fact;
    private double weight_norm;

    private double waste;
    private double wastePerKM;
}
