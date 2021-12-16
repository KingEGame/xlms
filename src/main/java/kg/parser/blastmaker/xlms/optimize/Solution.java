package kg.parser.blastmaker.xlms.optimize;


/**
 * Рптимальные решения планирования смено-суточных параметров ЭТК
 */
public class Solution {

    /**
     * Z_пj и M_пj - планок значения
     * Z_ij - относительные отклонения суточных затрат
     * M_ij - погрузочные-транспортные работы и их объемы
     * @param mass_truck
     * @param speed
     * @param distance
     * @param mass_ex
     * @param mass_truck_nom
     * @return
     */
    public double effect_plan(double mass_truck, double speed, double distance, double mass_ex, double mass_truck_nom){
        Waste waste = new Waste();
        return Math.pow(1 - waste.model_day_waste(mass_truck, speed, distance, mass_ex, mass_truck_nom)/(1 /*TODO S_pj + M_pj */ + 1), 2) + Math.pow((1- (1 /* TODO M_ij / M_pj *// 1)), 2);
    }


    /**
     * Эффективными будт считать такие поановые параметры автоколнные из н типа самосвалов (v_aij, N_aij) При к-ом экскаваторе,
     * которые доставля.т минимальные функции, при заланной  номинальной кмкости ковша - m_кj,
     * плане выемочный работ М_эi, пло=ановом премени Т_пб длительности цикла черпания t_ц, и плеча возки L_j
     *
     * @param mass_ex_nom
     * @param plan_take_work
     * @param mass_truck
     * @param speed
     * @param distance
     * @param mass_ex
     * @param mass_truck_nom
     * @return
     */
    public double min_value_parrameters(double mass_ex_nom, double  plan_take_work, double mass_truck, double speed, double distance, double mass_ex, double mass_truck_nom) {
        return /* TODO нужно найти минимальное значение */ effect_plan(mass_truck, speed , distance, mass_ex, mass_truck_nom );
    }
}
