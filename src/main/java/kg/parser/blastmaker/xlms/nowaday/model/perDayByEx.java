package kg.parser.blastmaker.xlms.nowaday.model;

import kg.parser.blastmaker.xlms.model.TruckForEx;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class perDayByEx {

    private String name_ex;

    private double speed;

    private String typeOfWork;

    private double timeInHours; // for truck todo

    private String driver_ex;

    private double weight_fact;

    private double weight_fact_avarage;

    private double weight_norm;

    private double weight_norm_avarage;

    private List<TruckForEx> trucks;

    private int gas;

    private double gas_avarage;

    private double distance;

    private double distance_avarage;
}
