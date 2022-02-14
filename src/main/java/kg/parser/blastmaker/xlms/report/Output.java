package kg.parser.blastmaker.xlms.report;

import kg.parser.blastmaker.xlms.model.*;
import kg.parser.blastmaker.xlms.model.Truck;
import kg.parser.blastmaker.xlms.objects.*;
import kg.parser.blastmaker.xlms.objects.Object;
import kg.parser.blastmaker.xlms.optimize.*;
import kg.parser.blastmaker.xlms.service.*;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class Output {

    @Autowired
    ObjectService objectService;

    @Autowired
    TruckService truckService;

    @Autowired
    ExcavatorService excavatorService;

    @Autowired
    OriginalData calculation;

    @Autowired
    Equation equation;

    @Autowired
    Optimal optimal;

    @Autowired
    Solution solution;


    /**
     * Функция для рассчета суточных затрат по эксковатору
     * входные данные (день)
     * выходные данные список эксковаторов @perDayByEx  с ему принадлашим сасмосвалами (тоже список @TruckForEx)
     *
     * */

    public List<Ex> waste(int day){

        List<Ex> objects = calculation.perDayByExes(day);

        List<Ex> temp = new ArrayList<>();
        boolean checked = false;
        for (Ex perDayEx : objects) {
            Excavator ex = excavatorService.getByName(perDayEx.getType());

            for (Truck str : perDayEx.getTrucks()) {
                TruckDTO truckDTO = truckService.getByName(str.getName());
                ComingTruck comeTruck = get_come(new Timestamp(objectService.getAllDate().get(day - 1).getTime()), str.getName());

                double answer =
                        equation.model_day_waste(Precision.round(perDayEx.getWeight_fact_avarage(), 3), str.getSpeed(), perDayEx.getDistance_avarage(),
                                Precision.round(ex.getCarrying_capacity_max() / 1000, 3), Precision.round(truckDTO.getNormal_weight() / 1000, 3), Precision.round(perDayEx.getGas_avarage(), 3), Precision.round(perDayEx.getTimeInHours(), 3), comeTruck.getGas(), comeTruck.getSpeed(), comeTruck.getTimeInHours()); // todo

                str.setWaste(Precision.round(answer, 3));

                str.setWastePerKM(Precision.round(str.getWaste()/(perDayEx.getDistance()*perDayEx.getWeight_fact()),3));
                checked =true;

            }
            if(checked){
                temp.add(perDayEx);
                checked = false;
            }
        }

        return temp;
    }


    /**
     * Функция для Эрлана
     * Высчета удельного расхода горючего по каждому рейсу дня по определенному самосвалу.
     * Для проверки
     *  TODO Эрлан
     */
    public void month(){
        List<Month> month = new ArrayList<>();

        int days = 30;

        for(int i = 0; i <= days; i++){
            List<Integer> nums = objectService.getNums(new Timestamp(objectService.getAllDate().get(i).getTime()));

            HashMap<String, List<Truck>> day = new HashMap<>();
            List<Truck> trucks = new ArrayList<>();
            for(int num : nums){
                List<Object> objects = objectService.findByTimeOfComeLoadingaAndNumSamosval(new Timestamp(objectService.getAllDate().get(i).getTime()), num);

                TruckDTO truckDTO = truckService.getByName(objects.get(0).getTypeSamosval());
                Excavator excavator = excavatorService.getByName(objects.get(0).getNumEx());
                Truck truck = new Truck();

                Object temp = null;
                boolean check = true;
                for(Object ob : objects){

                    truck.setName(ob.getTypeSamosval());
                    truck.setDistance(ob.getDistance()+truck.getDistance());

                    truck.setTypeOfWork(ob.getTypeOfWork());

                    Duration time = Duration.between(ob.getTimeOfComeLoading().toInstant(), ob.getTimeOfBeginUnloading().toInstant());
                    double hours = (double) time.getSeconds() / 3600;
                    double speed = ob.getDistance()/hours;
                    truck.setSpeed(speed + truck.getSpeed());

                    truck.setWeight_fact(ob.getWeightFact() + truck.getWeight_fact());
                    truck.setWeight_norm(ob.getWeightNorm() + truck.getWeight_norm());

                    double waste_gas = ob.getGasForBeginLoading() - ob.getGasForBeginUnloading();
                    truck.setWaste_gas(waste_gas + truck.getAvg_waste_gas());

                    truck.setSpecific_waste_with_mass(equation.specific_gas(waste_gas, ob.getWeightFact()+(truckDTO.getNormal_weight()/1000), hours, speed) + truck.getSpecific_waste_with_mass());

                    if(check){
                        check = false;
                        temp = ob;
                        continue;
                    }

                    Duration time_come = Duration.between(temp.getTimeOfBeginUnloading().toInstant(), ob.getTimeOfComeLoading().toInstant());
                    double hours_come = (double) time_come.getSeconds() / 3600;
                    double speed_come = temp.getDistance()/hours;
                    if(temp.getGasForBeginUnloading() - ob.getGasForBeginLoading() > 0)
                        truck.setSpecific_waste_without_mass(equation.specific_gas(temp.getGasForBeginUnloading() - ob.getGasForBeginLoading(), truckDTO.getNormal_weight()/1000, hours_come, speed_come) + truck.getSpecific_waste_without_mass());

                    temp = ob;
                }

                truck.setReice(objects.size());
                truck.setSpeed(truck.getSpeed()/objects.size());
                truck.setAvg_waste_gas(truck.getWaste_gas()/objects.size());
                truck.setSpecific_waste_with_mass(truck.getSpecific_waste_with_mass()/objects.size());
                truck.setSpecific_waste_without_mass(truck.getSpecific_waste_without_mass()/objects.size());

                double waste = equation.model_day_waste(truck.getSpeed(), truckDTO.getNormal_weight(), excavator.getCarrying_capacity_max(), truck.getDistance(), truck.getSpecific_waste_with_mass(), truck.getSpecific_waste_without_mass());

                truck.setWaste(waste + truck.getWaste());

                trucks.add(truck);

                if(!day.containsKey(truck.getTypeOfWork())){
                    day.put(truck.getTypeOfWork(), new ArrayList<>());
                    day.get(truck.getTypeOfWork()).add(truck);
                }else {
                    day.get(truck.getTypeOfWork()).add(truck);
                }
            }

        }

    }

    /**
     *
     * Функция для высчета месячных затрат по суткам
     * Работает на данный момент не корректно
     *
     * @return - возвращает список обектов Waste
     */

    public List<Waste> wasteMonth() {

        List<Waste> month = new ArrayList<>();

        List<Timestamp> dates = new ArrayList<>();
        for (Date date : objectService.getAllDate()) {
            dates.add(new Timestamp(date.getTime()));
        }

        for (int i = 1; i <= dates.size(); i++) {

            double waste = 0;
            double waste_per = 0;
            double weight_fact = 0;
            double weight_norm = 0;

            List<Ex> exes = new ArrayList<>();
            List<Ex> perByExes = waste(i);

            for (Ex ex : perByExes){

                double waste_inEx = 0;
                double waste_per_inEx = 0;
                double weightFact_inEx = 0;
                double weightNorm_InEx = 0;
                double distance = 0;



                List<Truck> truks = new ArrayList<>();

                for(Truck truck : ex.getTrucks()){
                    waste_inEx += truck.getWaste();
                    waste_per_inEx += truck.getWaste()/(truck.getDistance()*truck.getWeight_fact());
                    weightFact_inEx += truck.getWeight_fact();
                    weightNorm_InEx += truck.getWeight_norm();
                    distance += truck.getDistance();
                    truks.add(truck);

                }
                weight_fact += weightFact_inEx;
                weight_norm += weightNorm_InEx;
                waste += waste_inEx/truks.size();
                waste_per += waste_per_inEx/ truks.size();
                exes.add(Ex.builder().type(ex.getType()).distance(distance).weightFact(weightFact_inEx).weightNorm(weightNorm_InEx).waste(Precision.round(waste_inEx/truks.size(), 3)).wastePerKM(Precision.round(waste_per_inEx/truks.size(),3)).trucks(truks).typeofWork(ex.getTypeofWork()).build());
            }
            month.add(Waste.builder().ex(exes).day(i).waste(Precision.round(waste/perByExes.size(),3)).weight_fact(weight_fact).weight_norm(weight_norm).wastePerMKM(Precision.round(waste_per/perByExes.size(),3)).build());
        }


        return month;
    }

    /**
     * Функция для взятия данных о поррожнем вовращении самосвала
     * Все данные усредненные по сутке
     *
     * Входные данные (день, тип самосвала)
     *
     * @return - возвращает обьект ComingTruck который высчитывается для порожнего самосвала
     * */

    private ComingTruck get_come(Timestamp date, String truckName){
        List<Object> objects = objectService.getObjectsByDateAndByTypeTruck(date, truckName);


        ComingTruck comeTruck = ComingTruck.builder()
                .speed(0)
                .distance(0)
                .gas(0)
                .timeInHours(0)
                .build();

        boolean first = true;

        int count = 1;

        Object temp = null;
        for(Object ob : objects){

            if(first){
                temp = ob;
                first = false;
                continue;
            }

            if(ob.getReise() == temp.getReise()+1) {

                if(temp.getGasForBeginUnloading() - ob.getGasForBeginLoading() <= 0){
                    continue;
                }
                Duration time = Duration.between(temp.getTimeOfBeginUnloading().toInstant(), ob.getTimeOfComeLoading().toInstant());

                comeTruck.setTimeInHours(Precision.round((double) Math.abs(time.getSeconds()) / 3600, 3) + comeTruck.getTimeInHours());
                comeTruck.setSpeed((temp.getDistance() / Precision.round((double) Math.abs(time.getSeconds()) / 3600, 3)) + comeTruck.getSpeed());
                comeTruck.setGas(temp.getGasForBeginUnloading() - ob.getGasForBeginLoading() + comeTruck.getGas());
                comeTruck.setDistance(temp.getDistance() + comeTruck.getDistance());

                count++;
            }

            temp = ob;

        }

        comeTruck.setSpeed(Precision.round(comeTruck.getSpeed()/count, 3));
        comeTruck.setTimeInHours(Precision.round((comeTruck.getTimeInHours()/count)/2, 3));// todo changed
        comeTruck.setGas(Precision.round(comeTruck.getGas()/count,3));
        comeTruck.setDistance(Precision.round(comeTruck.getDistance()/count,3));

        return comeTruck;
    }
}
