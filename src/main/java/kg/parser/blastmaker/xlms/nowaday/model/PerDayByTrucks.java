package kg.parser.blastmaker.xlms.nowaday.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerDayByTrucks {

    private int num;

    private String model;

    private String ex;

    private int count;

    private double avg_speed;

    private double avg_waste_gas;


}
