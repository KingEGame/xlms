package kg.parser.blastmaker.xlms.report;

import kg.parser.blastmaker.xlms.model.Ex;
import kg.parser.blastmaker.xlms.model.Truck;
import kg.parser.blastmaker.xlms.model.Month;
import kg.parser.blastmaker.xlms.model.PerReice;
import kg.parser.blastmaker.xlms.objects.Object;
import kg.parser.blastmaker.xlms.objects.TruckDTO;
import kg.parser.blastmaker.xlms.service.ObjectService;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OriginalData {

    @Autowired
    private ObjectService objectService;


    /**
     * Функция для расчета выходного месячного документа, также выбока по типам работ
     *
     * @param type можно передать "" без выборки, либо "Тип работы"
     * @return
     */
    public HashMap<Integer, Month> month(String type){
        HashMap<Integer, Month> month = new HashMap<>();

      int days = 1;
      double distance = 0;
      int gas = 0;
      List<String> trucks = new ArrayList<>();
      List<String> exs = new ArrayList<>();
      long weightFact = 0;
      double weightNorm = 0;


      List<Timestamp> dates = new ArrayList<>();
      for(Date date : objectService.getAllDate()){
          dates.add(new Timestamp(date.getTime()));
      }

      for(Timestamp date : dates){
          List<Object> objects;
          if(type.equals("")) {
              objects = objectService.findByTimeOfComeLoading(date);
          }else {
              objects = objectService.getBytimeAndType(type, date);
          }
          for(Object object: objects){
              distance += object.getDistance();
              gas += Math.abs(object.getGasForBeginLoading() - object.getGasForBeginUnloading());
              if(!trucks.contains(object.getTypeSamosval()))
                  trucks.add(object.getTypeSamosval());
              if(!exs.contains(object.getNumEx())){
                  exs.add(object.getNumEx());
              }
              weightFact += object.getWeightFact();
              weightNorm += object.getWeightNorm();
          }

          List<String> temp_trucks = new ArrayList<>(trucks);
          trucks.clear();
          for(String str : temp_trucks){
              int count = objectService.getCountSamosvalByTimeAndType(date, str);
              trucks.add(str+" - "+count);
          }

          List<String> temp_exs = new ArrayList<>(exs);
          exs.clear();
          for(String ex : temp_exs){
              int count = objectService.getCountEx(date, ex);
              exs.add(ex+" - "+count);
          }


          month.put(days, Month.builder()
                  .trucks(new ArrayList<>(trucks))
                  .exs(new ArrayList<>(exs))
                  .distance(Precision.round(distance, 3))
                  .gas(gas)
                  .quantityReise(objectService.getReises(date))
                  .weightFact(weightFact)
                  .weightNorm(Precision.round(weightNorm, 3))
                  .build());
          days++;
          distance = 0;
          gas = 0;
          trucks.clear();
          exs.clear();
          weightFact = 0;
          weightNorm = 0;
      }

        return month;
    }

    public List<Truck> perDayByTruck(int days){
        List<Truck> views = new ArrayList<>();

        List<Integer> nums = objectService.getNums(new Timestamp(objectService.getAllDate().get(days-1).getTime()));

        for(Integer num : nums) {
            List<Object> orderByNum = objectService.findByTimeOfComeLoadingaAndNumSamosval(new Timestamp(objectService.getAllDate().get(days - 1).getTime()), num);

            double speed = 0;
            double coust_gas = 0;
            for(Object ob : orderByNum){
                double hours;

                Duration time = Duration.between(ob.getTimeOfComeLoading().toInstant(), ob.getTimeOfBeginUnloading().toInstant());

                hours = (double) time.getSeconds() / 3600;

                speed += ob.getDistance()/hours;
                coust_gas += Math.abs(ob.getGasForBeginLoading() - ob.getGasForBeginUnloading());
            }

            views.add(Truck.builder()
                    .count(objectService.getMaxReise(new Timestamp(objectService.getAllDate().get(days-1).getTime()), num))
                    .model(orderByNum.get(0).getTypeSamosval())
                    .ex(orderByNum.get(0).getNumEx())
                    .num(num)
                    .avg_speed(Precision.round(speed / orderByNum.size(), 6))
                    .avg_waste_gas(Precision.round(coust_gas / orderByNum.size(), 6))
                    .build());

        }

        return views;
    }


    /**
     * Функция для рвсчета выходного документа по эксковаторам для суток с его связкой самосвалов
     *
     * @param day - день
     * @return список обьектов типа Ex
     */
    public List<Ex> perDayByExes(int day){
        List<Ex> views = new ArrayList<>();

        List<String> nums = objectService.getEx(new Timestamp(objectService.getAllDate().get(day-1).getTime()));

        for(String ex : nums) {
            List<Object> orderByEx = objectService.getObjectsForDayByEx(new Timestamp(objectService.getAllDate().get(day - 1).getTime()), ex);

            double speed = 0;
            double coust_gas = 0;
            double weight_fact = 0;
            double weight_norm = 0;
            double distance = 0;
            String name = "";
            String typeOfWork = "";
            int reice = 0;
            for(Object ob : orderByEx){
                double hours;

                Duration time = Duration.between(ob.getTimeOfComeLoading().toInstant(), ob.getTimeOfBeginUnloading().toInstant());

                if(ob.getGasForBeginLoading() - ob.getGasForBeginUnloading() <=0){
                    continue;
                }
                hours = (double) time.getSeconds() / 3600;

                speed += ob.getDistance()/hours;
                coust_gas += ob.getGasForBeginLoading() - ob.getGasForBeginUnloading();
                weight_fact += ob.getWeightFact();
                weight_norm += ob.getWeightNorm();
                distance += ob.getDistance();
                name = ob.getNumEx();
                typeOfWork = ob.getTypeOfWork();
                reice++;
            }

            List<String> trucks = objectService.getTypesOfSamByexForDay(new Timestamp(objectService.getAllDate().get(day - 1).getTime()), ex);
            List<kg.parser.blastmaker.xlms.model.Truck> tr_temp = new ArrayList<>(trucks.size());

            for(String tr : trucks){
                tr_temp.add(kg.parser.blastmaker.xlms.model.Truck.builder().reice(reice).weight_fact(weight_fact).weight_norm(weight_norm).speed(Precision.round(speed/orderByEx.size(), 3)).quantity(objectService.getQuantityTruckFromExForDay(new Timestamp(objectService.getAllDate().get(day - 1).getTime()), ex, tr)).name(tr).distance(Precision.round(distance,3)).build());
            }

            views.add(Ex.builder()
                    .type(name)
                    .driver_name(ex)
                            .weight_fact_avarage(weight_fact/orderByEx.size())
                            .weight_norm_avarage(weight_norm/orderByEx.size())
                            .typeofWork(typeOfWork)
                    .speed(speed/orderByEx.size())
                            .timeInHours(distance/speed)
                    .trucks(new ArrayList<>(tr_temp))
                    .weight_fact(weight_fact)
                    .weight_norm(Precision.round(weight_norm, 6))
                    .gas((int)coust_gas)
                    .gas_avarage(coust_gas/orderByEx.size())
                    .distance(Precision.round(distance, 6))
                    .distance_avarage(Precision.round(distance, 6)/orderByEx.size())
                    .build());

        }

        return views;
    }


    /**
     * для выходного документа по самосвалу по рейсово
     *
     * @param days - день
     * @param num - номер самосвала
     * @return - список обьекта PerReice
     */
    public List<PerReice> perReice(int days, int num){
        List<PerReice> perReises = new ArrayList<>();

        List<Object> objects = objectService.findByTimeOfComeLoadingaAndNumSamosval(new Timestamp(objectService.getAllDate().get(days - 1).getTime()), num);

        for(Object ob : objects){
            double hours;

            Duration time = Duration.between(ob.getTimeOfComeLoading().toInstant(), ob.getTimeOfBeginUnloading().toInstant());

            hours = (double) time.getSeconds() / 3600;

            double speed = ob.getDistance()/hours;
            double gasoline = Math.abs(ob.getGasForBeginLoading() - ob.getGasForBeginUnloading());

            perReises.add(PerReice.builder()
                    .distance(ob.getDistance())
                    .gasoline(gasoline)
                    .inHours(Precision.round(hours, 6))
                    .time(new Time( (int)time.getSeconds() / 3600,(int)(time.getSeconds() - (time.getSeconds() / 3600) *3600) / 60, (int) (time.getSeconds() - (((time.getSeconds() - (time.getSeconds() / 3600) *3600) / 60)*60) )))
                    .model_truck(ob.getTypeSamosval())
                    .name_driver(ob.getNameDriverSamosval())
                    .ex(ob.getNumEx())
                    .ex_driver(ob.getNameEx())
                    .reise(ob.getReise())
                    .num(ob.getNumSamosval())
                    .speedWithWeith(Precision.round(speed,6))
                    .build());
        }

        return perReises;
    }




}


