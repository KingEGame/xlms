package kg.parser.blastmaker.xlms.controller;

import kg.parser.blastmaker.xlms.model.ForComeSum;
import kg.parser.blastmaker.xlms.model.TruckForEx;
import kg.parser.blastmaker.xlms.nowaday.Calculation;
import kg.parser.blastmaker.xlms.nowaday.perDayByEx;
import kg.parser.blastmaker.xlms.objects.Excavator;
import kg.parser.blastmaker.xlms.objects.Object;
import kg.parser.blastmaker.xlms.objects.Truck;
import kg.parser.blastmaker.xlms.optimize.Optimal;
import kg.parser.blastmaker.xlms.optimize.Solution;
import kg.parser.blastmaker.xlms.optimize.Waste;
import kg.parser.blastmaker.xlms.service.ExcavatorService;
import kg.parser.blastmaker.xlms.service.ObjectService;
import kg.parser.blastmaker.xlms.service.TruckService;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OptimizeController {

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

    @GetMapping("calc/perdayByEx/{day}/optimize")
    String optimize(Model model, @PathVariable(value = "day") int day){
       List<perDayByEx> objects = calculation.perDayByExes(day);


       List<perDayByEx> temp = new ArrayList<>();
       boolean checked = false;
       for (perDayByEx perDayEx : objects) {
           if( perDayEx.getName_ex().equals("1") || perDayEx.getName_ex().equals("2")|| perDayEx.getName_ex().equals("PC1250_№1")) {
               Excavator ex = excavatorService.getByName(perDayEx.getName_ex());

               for (TruckForEx str : perDayEx.getTrucks()) {
                    if(str.getName().equals("TEREX  TR100") ||str.getName().equals("TEREX TR100Ш") ||str.getName().equals("БелАЗ-75131") ||str.getName().equals("БелАЗ-75131-50М")) {
                        Truck truck = truckService.getByName(str.getName());
                        ForComeSum comeTruck = get_come(new Timestamp(objectService.getAllDate().get(day - 1).getTime()), str.getName());

                        double answer =
                                waste.model_day_waste(Precision.round(perDayEx.getWeight_fact_avarage(), 3), str.getSpeed(), perDayEx.getDistance_avarage(),
                                        Precision.round(ex.getCarrying_capacity_max() / 1000, 3), Precision.round(truck.getNormal_weight() / 1000, 3), Precision.round(perDayEx.getGas_avarage(), 3), Precision.round(perDayEx.getTimeInHours(), 3), comeTruck.getGas(), comeTruck.getSpeed(), comeTruck.getTimeInHours()); // todo

                        str.setWaste(Precision.round(answer, 3));

                        str.setWastePerKM(Precision.round(str.getWaste()/(perDayEx.getDistance()*perDayEx.getWeight_fact()),3));
                        checked =true;
                    }
               }
           }
           if(checked){
               temp.add(perDayEx);
               checked = false;
           }
       }

       model.addAttribute("items", temp);
       model.addAttribute("day", day);

        return "forDays\\perDayByExes";
    }



    private ForComeSum get_come(Timestamp date, String truckName){
        List<Object> objects = null;
        if(truckName.equals("TEREX TR100Ш")){
            objects = objectService.findByTimeOfComeLoadingaAndNumSamosval(date, 168);
        }else {
            objects = objectService.getObjectsByDateAndByTypeTruck(date, truckName);
        }

        ForComeSum comeTruck = ForComeSum.builder()
                                .speed(0)
                                .distance(0)
                                .gas(0)
                                .timeInHours(0)
                                .build();

        Truck truck = truckService.getByName(truckName);

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
