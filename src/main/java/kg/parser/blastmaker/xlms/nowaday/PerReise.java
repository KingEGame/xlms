package kg.parser.blastmaker.xlms.nowaday;

import lombok.*;
import org.springframework.stereotype.Service;

import java.sql.Time;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerReise {

    private int num;

    private int reise;

    private String model_truck;

    private String name_driver;

    private String ex;

    private String ex_driver;

    private double speedWithWeith;

    private double inHours;

    private Time time;

    private double gasoline;

    private double distance;

}
