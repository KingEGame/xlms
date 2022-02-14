package kg.parser.blastmaker.xlms.optimize;

import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

/**
    Вычисление затрат
 */
@Service
public class Equation {

    /**
     * *
     * это суточное количество самосвалов i-го типа для обслуживания экскаватора j-го типа
     *
     * @param distance - растояние которое должен пройти самосвал
     * @param mass_truck_norm - грузоподьемность саосвала нормальная
     * @param mass_ex_max - грузоподьемность ковша максимальная
     * @param speed -средняя скорость самосвала
     * @return
     */
    public double quality_trucks(double distance, double mass_ex_max, double mass_truck_norm, double speed){
        return Precision.round(2*distance*1*mass_ex_max, 3)/Precision.round(1*  mass_truck_norm * speed *Constants.t_time_cycle, 3); // todo contants need to change!!!
    }

    /**
     *стоимость горючего, израсходованного на перевозку плановой массы горного материала
     * 〖0,5c〗_e m_aiн v_aij^2 [q_ei k_зi+0,73(q_ei+q_iп )]
     *
     * @param mass_truck_norm - номнальная грузоподьемность самосвала
     * @param speed_go - средняяя скорость дивжени самосвала
     * @param mass_truck_max
     * @param speed_come
     * @param timeInhours_come
     * @param timeInHours_go
     * @param waste_gas_with_mass
     * @param waste_gas_without_mass
     *               todo need add as param the waste the empty truck gasoline!!!
     * @return
     */
    public double cost_gas(double mass_truck_norm, double speed_go , double waste_gas_with_mass, double waste_gas_without_mass, double timeInHours_go,
                           double timeInhours_come, double mass_truck_max, double speed_come){
        return 0.5*Constants.cost_gas*mass_truck_norm *
                Precision.round(Math.pow(speed_go,2),2) *(waste_constant_gas_with_mass(waste_gas_with_mass, mass_truck_max, timeInHours_go, speed_go)*1 +
                Precision.round(0.73*(waste_constant_gas_with_mass(waste_gas_with_mass, mass_truck_max, timeInHours_go, speed_go)+
                        waste_constant_gas_without_mass(waste_gas_without_mass, mass_truck_norm, timeInhours_come, speed_come)),4));
    }

    public double cost_gas(double mass_truck_norm, double speed, double specific_waste_with_mass, double specific_waste_without_mass){
        return 0.5*Constants.cost_gas*mass_truck_norm *
                Precision.round(Math.pow(speed,2),2) *(specific_waste_with_mass*1 + Precision.round(0.73*(specific_waste_with_mass+ specific_waste_without_mass),4));
    }

    /**
     *связанные с обеспечением готовности транспортных средств ГТК
     * Z_тг=a_0+a_1 exp(-a_2 m_aij );
     * @param mass_truck_norm - номинальная грузоподьемность самосвала
     * @return
     */
    public double waste_for_ready_venicle(double mass_truck_norm){
        return (Constants.approc_waste_ready_class0 + Constants.approc_waste_ready_class1*Precision.round(Math.pow(Math.E, -(Constants.approc_waste_ready_class2 * mass_truck_norm)),2)) ;
    }

    /**
     *затраты, связанные с работой экскаватора
     * 〖10〗^3 4,5 ln(m_кj+1) T_п
     * @param mass_ex_max - грузоподьемность ковша максимальная
     * @return
     */
    public double waste_for_ex(double mass_ex_max){
        return (4500 * Math.log(mass_ex_max + 1))*Constants.plan_time_day_cycle;
    }

    /**
     * затраты горючего на работу самосвала с рудой
     * q_e = waste_gas/ )m_max * speed * time)
     * @param mass_truck_max
     * @param speed
     * @param waste_gas
     * @param timeInHours
     * */
    public double waste_constant_gas_with_mass(double waste_gas, double mass_truck_max, double timeInHours, double speed){
        return Precision.round(waste_gas/ Precision.round(mass_truck_max*Math.pow(speed, 2)*timeInHours, 5), 5);
    }

    /**
     * Затраты потраченные на топливо порожним самосвалом
     *q_e = waste_gas/ )m_max * speed * time)
     * @param timeInHours
     * @param waste_gas
     * @param speed
     * @param mass_truck_norm
     * */

    public double waste_constant_gas_without_mass(double waste_gas, double mass_truck_norm, double timeInHours, double speed){
        return Precision.round(waste_gas/Precision.round(mass_truck_norm*Math.pow(speed, 2)*timeInHours, 5),5);
    }


    /**
     * заданной суточной добычи
     *[〖0,5c〗_e m_aiн v_aij^2 [q_ei k_зi+0,73(q_ei+q_iп )]+[a_0+a_1 exp(-a_2 m_aiн )]] T_п N_аij+[〖10〗^3 4,5 ln⁡(m_кj+1) ] T_п
     * @param mass_truck_max - максимальная грухоподьемность самосвала
     * @param speed_go - средняя скорость самосвала
     * @param distance - растояние которое должен пройти самосвал
     * @param mass_ex_max - грузоподьмеость ковша экскаватора
     * @param mass_truck_norm - номинальная грузоподьмеость
     * @param timeInHours_go - время в часах которое он потратил на транспортировку руды
     * @param speed_come - скорость с которрой возвращался самосвал
     * @param timeInHours_come - время в часаъ, потраченное на возвращение
     * @param waste_gas_come - топливо потраченное на возвращение
     * @param waste_gas_go - топливо потраченное на транспортировку руды
     * @return
     */
    public double model_day_waste(double mass_truck_max, double speed_go, double distance, double mass_ex_max, double mass_truck_norm, double waste_gas_go,
                                  double timeInHours_go, double waste_gas_come, double speed_come, double timeInHours_come){
        return (Precision.round(cost_gas(mass_truck_norm, speed_go, waste_gas_go, waste_gas_come, timeInHours_go, timeInHours_come, mass_truck_max, speed_come),3) +
                Precision.round(waste_for_ready_venicle(mass_truck_norm),3)) *
                        Constants.plan_time_day_cycle*Precision.round(quality_trucks(distance, mass_ex_max, mass_truck_norm, speed_go), 3)+
                        Precision.round(waste_for_ex(mass_ex_max), 3);
    }


    public double model_day_waste(double speed, double mass_truck_norm, double mass_ex_max, double distance,double specific_waste_with_mass, double specific_waste_without_mass){
        return (Precision.round(cost_gas(mass_truck_norm, speed, specific_waste_with_mass,specific_waste_without_mass), 4) +
                Precision.round(waste_for_ready_venicle(mass_truck_norm),3)) *
                    Constants.plan_time_day_cycle*Precision.round(quality_trucks(distance, mass_ex_max, mass_truck_norm, speed), 3)+
                Precision.round(waste_for_ex(mass_ex_max), 3);
    }
}