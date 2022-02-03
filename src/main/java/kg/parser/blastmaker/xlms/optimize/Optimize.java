package kg.parser.blastmaker.xlms.optimize;

import kg.parser.blastmaker.xlms.model.Ex;
import kg.parser.blastmaker.xlms.model.ForComeSum;
import kg.parser.blastmaker.xlms.model.TruckForEx;
import kg.parser.blastmaker.xlms.model.WasteForMonth;
import kg.parser.blastmaker.xlms.nowaday.Calculation;
import kg.parser.blastmaker.xlms.nowaday.model.perDayByEx;
import kg.parser.blastmaker.xlms.objects.Excavator;
import kg.parser.blastmaker.xlms.objects.Object;
import kg.parser.blastmaker.xlms.objects.Truck;
import kg.parser.blastmaker.xlms.service.ExcavatorService;
import kg.parser.blastmaker.xlms.service.ObjectService;
import kg.parser.blastmaker.xlms.service.TruckService;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class Optimize {

    @Autowired
    ObjectService objectService;

    @Autowired
    TruckService truckService;

    @Autowired
    ExcavatorService excavatorService;

    @Autowired
    Calculation calculation;

    @Autowired
    Waste waste;

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

    public List<perDayByEx> waste(int day){

        List<perDayByEx> objects = calculation.perDayByExes(day);

        List<perDayByEx> temp = new ArrayList<>();
        boolean checked = false;
        for (perDayByEx perDayEx : objects) {
            Excavator ex = excavatorService.getByName(perDayEx.getName_ex());

            for (TruckForEx str : perDayEx.getTrucks()) {
                Truck truck = truckService.getByName(str.getName());
                ForComeSum comeTruck = get_come(new Timestamp(objectService.getAllDate().get(day - 1).getTime()), str.getName());

                double answer =
                        waste.model_day_waste(Precision.round(perDayEx.getWeight_fact_avarage(), 3), str.getSpeed(), perDayEx.getDistance_avarage(),
                                Precision.round(ex.getCarrying_capacity_max() / 1000, 3), Precision.round(truck.getNormal_weight() / 1000, 3), Precision.round(perDayEx.getGas_avarage(), 3), Precision.round(perDayEx.getTimeInHours(), 3), comeTruck.getGas(), comeTruck.getSpeed(), comeTruck.getTimeInHours()); // todo

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

    public List<WasteForMonth> wasteMonth() {

        List<WasteForMonth> month = new ArrayList<>();

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
            List<perDayByEx> perByExes = waste(i);

            for (perDayByEx ex : perByExes){

                double waste_inEx = 0;
                double waste_per_inEx = 0;
                double weightFact_inEx = 0;
                double weightNorm_InEx = 0;
                double distance = 0;



                List<TruckForEx> truks = new ArrayList<>();

                for(TruckForEx truck : ex.getTrucks()){
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
                waste_per += waste_per_inEx;
                exes.add(Ex.builder().name(ex.getName_ex()).distance(distance).weightFact(weightFact_inEx).weightNorm(weightNorm_InEx).waste(Precision.round(waste_inEx, 3)).wastePerKM(Precision.round(waste_per_inEx,3)).truks(truks).typeofWork(ex.getTypeOfWork()).build());
            }
            month.add(WasteForMonth.builder().ex(exes).day(i).waste(Precision.round(waste/perByExes.size(),3)).weight_fact(weight_fact).weight_norm(weight_norm).wastePerMKM(Precision.round(waste_per/perByExes.size(),3)).build());
        }


        return month;
    }

    /**
     * Функция для взятия данных о поррожнем вовращении самосвала
     * Все данные усредненные по сутке
     *
     * Входные данные (день, тип самосвала)
     * */

    private ForComeSum get_come(Timestamp date, String truckName){
        List<Object> objects = objectService.getObjectsByDateAndByTypeTruck(date, truckName);


        ForComeSum comeTruck = ForComeSum.builder()
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
                Duration time = Duration.between(temp.getTimeOfBeginUnloading().toInstant(), ob.getTimeOfComeLoading().toInstant());

                comeTruck.setTimeInHours(Precision.round((double) Math.abs(time.getSeconds()) / 3600, 5) + comeTruck.getTimeInHours());
                comeTruck.setSpeed((temp.getDistance() / Precision.round((double) Math.abs(time.getSeconds()) / 3600, 5)) + comeTruck.getSpeed());
                comeTruck.setGas(Math.abs(temp.getGasForBeginUnloading() - ob.getGasForBeginLoading()) + comeTruck.getGas());
                comeTruck.setDistance(temp.getDistance() + comeTruck.getDistance());

                count++;
            }

            temp = ob;

        }

        comeTruck.setSpeed(Precision.round(comeTruck.getSpeed()/count, 5));
        comeTruck.setTimeInHours(Precision.round(comeTruck.getTimeInHours()/count, 5));
        comeTruck.setGas(Precision.round(comeTruck.getGas()/count,3));
        comeTruck.setDistance(Precision.round(comeTruck.getDistance()/count,3));

        return comeTruck;
    }
}
