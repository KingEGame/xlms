package kg.parser.blastmaker.xlms.report;

import kg.parser.blastmaker.xlms.model.*;
import kg.parser.blastmaker.xlms.model.Truck;
import kg.parser.blastmaker.xlms.objects.*;
import kg.parser.blastmaker.xlms.objects.TruckTripsDTO;
import kg.parser.blastmaker.xlms.optimize.*;
import kg.parser.blastmaker.xlms.respositiry.ExcavatorRepository;
import kg.parser.blastmaker.xlms.respositiry.TruckTripsRespository;
import kg.parser.blastmaker.xlms.respositiry.TruckTypeRepository;
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
    OriginalData calculation;

    @Autowired
    Equation equation;

    @Autowired
    Optimal optimal;

    @Autowired
    Solution solution;

    @Autowired
    ExcavatorRepository excavatorService;

    @Autowired
    TruckTripsRespository objectService;

    @Autowired
    TruckTypeRepository truckService;


    /**
     * Функция для рассчета суточных затрат по эксковатору
     * входные данные (день)
     * выходные данные список эксковаторов @perDayByEx  с ему принадлашим сасмосвалами (тоже список @TruckForEx)
     *
     * */

    public List<Excavator> waste(int day){

        List<Excavator> objects = calculation.perDayByExes(day);

        List<Excavator> temp = new ArrayList<>();
        boolean checked = false;
        for (Excavator perDayExcavator : objects) {
            ExcavatorDTO ex = excavatorService.getByName(perDayExcavator.getType());

            for (Truck str : perDayExcavator.getTrucks()) {
//                TruckTypeDTO truckTypeDTO = truckService.getByName(str.getType_truck());
                Truck comeTruck = get_come(new Timestamp(objectService.findAllDateTime().get(day - 1).getTime()), str.getType_truck());

//                double answer =
//                        equation.model_day_waste(Precision.round(perDayExcavator.getWeight_fact_avarage(), 3), str.getSpeed(), perDayExcavator.getDistance_avarage(),
//                                Precision.round(ex.getCarrying_capacity_max() / 1000, 3), Precision.round(truckTypeDTO.getWeight() / 1000, 3), Precision.round(perDayExcavator.getGas_avarage(), 3), Precision.round(perDayExcavator.getTimeInHours(), 3), comeTruck.getWaste_gas_truck(), comeTruck.getSpeed(), comeTruck.getSpent_time_in_hour()); // todo

//                str.setWaste_truck(Precision.round(answer, 3));

                str.setCost_price(Precision.round(str.getWaste_truck()/(perDayExcavator.getDistance()* perDayExcavator.getWeight_fact()),3));
                checked =true;

            }
            if(checked){
                temp.add(perDayExcavator);
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
            List<Integer> nums = objectService.findNums(new Timestamp(objectService.findAllDateTime().get(i).getTime()));

            HashMap<String, List<Truck>> day = new HashMap<>();
            List<Truck> trucks = new ArrayList<>();
            for(int num : nums){
                List<TruckTripsDTO> truckTripsDTOS = objectService.findByTimeOfComeLoadingaAndNumSamosval(new Timestamp(objectService.findAllDateTime().get(i).getTime()), num);

                TruckTypeDTO truckTypeDTO = truckService.getByName(truckTripsDTOS.get(0).getTruck().getTruckTypeDTO().getName()).get(0);
                ExcavatorDTO excavator = excavatorService.getByName(truckTripsDTOS.get(0).getExcavator().getName());
                Truck truck = new Truck();

                TruckTripsDTO temp = null;
                boolean check = true;
                for(TruckTripsDTO ob : truckTripsDTOS){

                    truck.setType_truck(ob.getTruck().getTruckTypeDTO().getName());
                    truck.setDistance(ob.getTrip_distance()+truck.getDistance());

                    truck.setType_of_work(ob.getTypeOfWork().getWork_name());

                    Duration time = Duration.between(ob.getArrival_time().toInstant(), ob.getBegin_unloading().toInstant());
                    double hours = (double) time.getSeconds() / 3600;
                    double speed = ob.getTrip_distance()/hours;
                    truck.setSpeed(speed + truck.getSpeed());

                    truck.setWeight_fact(ob.getActual_weight() + truck.getWeight_fact());
                    truck.setWeight_norm(ob.getTruck().getTruckTypeDTO().getRated_load() + truck.getWeight_norm());

                    double waste_gas = ob.getFuel_at_loading() - ob.getFuel_at_unloading();
                    truck.setWaste_gas_truck(waste_gas + truck.getAvg_waste_gas_for_reice());

                    truck.setSpecific_waste_with_mass(equation.specific_gas(waste_gas, ob.getActual_weight()+(truckTypeDTO.getWeight()/1000), hours, speed) + truck.getSpecific_waste_with_mass());

                    if(check){
                        check = false;
                        temp = ob;
                        continue;
                    }

                    Duration time_come = Duration.between(temp.getBegin_unloading().toInstant(), ob.getArrival_time().toInstant());
                    double hours_come = (double) time_come.getSeconds() / 3600;
                    double speed_come = temp.getTrip_distance()/hours;
                    if(temp.getFuel_at_unloading() - ob.getFuel_at_loading() > 0)
                        truck.setSpecific_waste_without_mass(equation.specific_gas(temp.getFuel_at_unloading() - ob.getFuel_at_loading(), truckTypeDTO.getWeight()/1000, hours_come, speed_come) + truck.getSpecific_waste_without_mass());

                    temp = ob;
                }

                truck.setCount_reice(truckTripsDTOS.size());
                truck.setSpeed(truck.getSpeed()/ truckTripsDTOS.size());
                truck.setAvg_waste_gas_for_reice(truck.getWaste_gas_truck()/ truckTripsDTOS.size());
                truck.setSpecific_waste_with_mass(truck.getSpecific_waste_with_mass()/ truckTripsDTOS.size());
                truck.setSpecific_waste_without_mass(truck.getSpecific_waste_without_mass()/ truckTripsDTOS.size());

                double waste = equation.model_day_waste(truck.getSpeed(), truckTypeDTO.getWeight(), excavator.getCarrying_capacity_max(), truck.getDistance(), truck.getSpecific_waste_with_mass(), truck.getSpecific_waste_without_mass());

                truck.setWaste_truck(waste + truck.getWaste_truck());

                trucks.add(truck);

                if(!day.containsKey(truck.getType_of_work())){
                    day.put(truck.getType_of_work(), new ArrayList<>());
                    day.get(truck.getType_of_work()).add(truck);
                }else {
                    day.get(truck.getType_of_work()).add(truck);
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
        for (Date date : objectService.findAllDateTime()) {
            dates.add(new Timestamp(date.getTime()));
        }

        for (int i = 1; i <= dates.size(); i++) {

            double waste = 0;
            double waste_per = 0;
            double weight_fact = 0;
            double weight_norm = 0;

            List<Excavator> excavators = new ArrayList<>();
            List<Excavator> perByExcavators = waste(i);

            for (Excavator excavator : perByExcavators){

                double waste_inEx = 0;
                double waste_per_inEx = 0;
                double weightFact_inEx = 0;
                double weightNorm_InEx = 0;
                double distance = 0;



                List<Truck> truks = new ArrayList<>();

                for(Truck truck : excavator.getTrucks()){
                    waste_inEx += truck.getWaste_truck();
                    waste_per_inEx += truck.getWaste_truck()/(truck.getDistance()*truck.getWeight_fact());
                    weightFact_inEx += truck.getWeight_fact();
                    weightNorm_InEx += truck.getWeight_norm();
                    distance += truck.getDistance();
                    truks.add(truck);

                }
                weight_fact += weightFact_inEx;
                weight_norm += weightNorm_InEx;
                waste += waste_inEx/truks.size();
                waste_per += waste_per_inEx/ truks.size();
                excavators.add(Excavator.builder().type(excavator.getType()).distance(distance).weightFact(weightFact_inEx).weightNorm(weightNorm_InEx).waste(Precision.round(waste_inEx/truks.size(), 3)).wastePerKM(Precision.round(waste_per_inEx/truks.size(),3)).trucks(truks).typeofWork(excavator.getTypeofWork()).build());
            }
            month.add(Waste.builder().excavators(excavators).day(i).waste(Precision.round(waste/ perByExcavators.size(),3)).weight_fact(weight_fact).weight_norm(weight_norm).wastePerMKM(Precision.round(waste_per/ perByExcavators.size(),3)).build());
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

    private Truck get_come(Timestamp date, String truckName){
        List<TruckTripsDTO> truckTripsDTOS = objectService.getObjectsByDateAndByTypeTruck(date, truckName);


        Truck comeTruck = Truck.builder()
                .speed(0)
                .distance(0)
                .waste_gas_truck(0)
                .spent_time_in_hour(0)
                .build();

        boolean first = true;

        int count = 1;

        TruckTripsDTO temp = null;
        for(TruckTripsDTO ob : truckTripsDTOS){

            if(first){
                temp = ob;
                first = false;
                continue;
            }

            if(ob.getTrip_number() == temp.getTrip_number()+1) {

                if(temp.getFuel_at_unloading() - ob.getFuel_at_loading() <= 0){
                    continue;
                }
                Duration time = Duration.between(temp.getBegin_unloading().toInstant(), ob.getArrival_time().toInstant());

                comeTruck.setSpent_time_in_hour(Precision.round((double) Math.abs(time.getSeconds()) / 3600, 3) + comeTruck.getSpent_time_in_hour());
                comeTruck.setSpeed((temp.getTrip_distance() / Precision.round((double) Math.abs(time.getSeconds()) / 3600, 3)) + comeTruck.getSpeed());
                comeTruck.setWaste_gas_truck(temp.getFuel_at_unloading() - ob.getFuel_at_loading() + comeTruck.getWaste_gas_truck());
                comeTruck.setDistance(temp.getTrip_distance() + comeTruck.getDistance());

                count++;
            }

            temp = ob;

        }

        comeTruck.setSpeed(Precision.round(comeTruck.getSpeed()/count, 3));
        comeTruck.setSpent_time_in_hour(Precision.round((comeTruck.getSpent_time_in_hour()/count)/2, 3));// todo changed
        comeTruck.setWaste_gas_truck(Precision.round(comeTruck.getWaste_gas_truck()/count,3));
        comeTruck.setDistance(Precision.round(comeTruck.getDistance()/count,3));

        return comeTruck;
    }
}
