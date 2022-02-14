package kg.parser.blastmaker.xlms.model;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComingTruck {

    private double speed;
    private double gas;
    private double timeInHours;
    private double distance;
}
