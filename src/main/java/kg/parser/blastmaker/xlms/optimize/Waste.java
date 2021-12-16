package kg.parser.blastmaker.xlms.optimize;

/**
    Вычисление затрат
 */
public class Waste {

    /**
     * *
     * это суточное количество самосвалов i-го типа для обслуживания экскаватора j-го типа
     *
     * @param distance - растояние которое должен пройти самосвал
     * @param mass_truck_max - грузоподьемность саосвала аксимальная
     * @param mass_ex - грузоподьемность саосвала
     * @param speed -средняя скорость самосвала
     * @return
     */
    public double quality_trucks(double distance, double mass_ex, double mass_truck_max, double speed){
        return (2*distance*Constants.fact_load_landle*mass_ex/*TODO грузопдбемность ковша */)/(Constants.fact_load_carcase*  mass_truck_max/* TODO  грузопдьемность самосвала максимальная*/*speed /* todo скорость самосвала */*Constants.t_time_cycle);
    }

    /**
     *стоимость горючего, израсходованного на перевозку плановой массы горного материала
     * 〖0,5c〗_e m_aiн v_aij^2 [q_ei k_зi+0,73(q_ei+q_iп )]
     *
     * @param mass_truck - максимальная грузоподьемность самосвала
     * @param speed_truck - средняяя скорость дивжени самосвала
     * @return
     */
    public double cost_gas(double mass_truck, double speed_truck){
        return 0.5*Constants.cost_gas*mass_truck/* todo номнальная грузопобемность самосвала*/ * Math.pow(speed_truck,2)
                *(Constants.specific_spentGas*Constants.fact_load_landle + 0.73*(Constants.specific_spentGas/* TODO удельный  путевой расход горючего самосвала*/+1/* TODO порожний путевой расход горючего самосвала*/ ));
    }

    /**
     *связанные с обеспечением готовности транспортных средств ГТК
     * Z_тг=a_0+a_1 exp(-a_2 m_aij );
     * @param mass_truck - номинальная грузоподьемность самосвала
     * @return
     */
    public double waste_for_ready_venicle(double mass_truck){
        return (Constants.approc_waste_ready_class0 + Constants.approc_waste_ready_class1*Math.pow(Math.E, -(Constants.approc_waste_ready_class2) * mass_truck /* TODO номинальная грузоподьемность самосвала*/));
    }

    /**
     *затраты, связанные с работой экскаватора
     * 〖10〗^3 4,5 ln(m_кj+1) T_п
     * @param mass_ex - грузоподьемность ковша максимальная
     * @return
     */
    private double waste_for_ex(double mass_ex){
        return (Math.pow(10, 3) * 4.5 * Math.log(mass_ex /* TODO грузоподьемность ковша максимальная*/ + 1))*Constants.plan_time_day_cycle;
    }


    /**
     * заданной суточной добычи
     *[〖0,5c〗_e m_aiн v_aij^2 [q_ei k_зi+0,73(q_ei+q_iп )]+[a_0+a_1 exp(-a_2 m_aiн )]] T_п N_аij+[〖10〗^3 4,5 ln⁡(m_кj+1) ] T_п
     * @param mass_truck - максимальная грухоподьемность самосвала
     * @param speed - средняя скорость самосвала
     * @param distance - растояние которое должен пройти самосвал
     * @param mass_ex - грузоподьмеость ковша экскаватора
     * @param mass_truck_nom - номинальная грузоподьмеость
     * @return
     */
    public double model_day_waste(double mass_truck, double speed, double distance, double mass_ex, double mass_truck_nom){
        return (cost_gas(mass_truck, speed) + waste_for_ready_venicle(mass_truck_nom)) *
                        Constants.plan_time_day_cycle*quality_trucks(distance, mass_ex, mass_truck, speed) +
                waste_for_ex(mass_truck);
    }
}
