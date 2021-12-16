package kg.parser.blastmaker.xlms.optimize;


/**
 * Рассчет оптимальных плановых значений смено-суточных параметров ЭТК
 */
public class Optimal {


    /**
     * оптимальная скорость дивжения самосвалов i-го типа при j-ом типе эксковатора
     *
     * @param mass_truck_max - максимальная грузоподьемность самосвала
     * @param mass_truck_nom - номинальная грузоподьемность самосвала
     * @param speed - средняяя скорость дивжени самосвала
     * @return
     */
    public double optimal_speed(double mass_truck_max, double mass_truck_nom, double speed){
        Waste waste = new Waste();
        return Math.sqrt(waste.waste_for_ready_venicle(mass_truck_nom)/waste.cost_gas(mass_truck_max, speed));
    }

    /**
     * *
     * оптимальное количество самосвалов i-го типа для обслуживания экскаватора j-го типа
     *
     * @param distance - растояние которое должен пройти самосвал
     * @param mass_truck_max - грузоподьемность саосвала аксимальная
     * @param mass_ex - грузоподьемность саосвала
     * @param speed -средняя скорость самосвала
     * @return
     */
    public  double optimal_quantity(double distance, double mass_ex, double mass_truck_max, double speed){
        Waste waste = new Waste();
        return waste.quality_trucks(distance, mass_ex, mass_truck_max, speed);
    }
}
