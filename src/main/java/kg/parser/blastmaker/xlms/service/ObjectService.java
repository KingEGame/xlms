package kg.parser.blastmaker.xlms.service;

import kg.parser.blastmaker.xlms.objects.Object;
import kg.parser.blastmaker.xlms.respositiry.ObjectRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ObjectService {

    @Autowired
    private ObjectRespository objectRespository;

    public Object save(Object payment){
        return objectRespository.saveAndFlush(payment);
    }

    public List<Object> saveAlL(List<Object> objects) {
        return objectRespository.saveAllAndFlush(objects);
    }

    public List<Object> getAll(){
        return objectRespository.findAllOrderBy();
    }

    public Object getByRequestId(Long requestId){
        return objectRespository.getById(requestId);
    }

    public List<Object> orderByTypeOfWork(String object){
        return objectRespository.findObjectByTypeOfWork(object);
    }

    public List<Date> getAllDate(){
        return objectRespository.findAllDateTime();
    }

    public List<Object> findByTimeOfComeLoading(Timestamp timestamp){
        return objectRespository.findByTimeOfComeLoading(timestamp);
    }

    public List<Object> findByTimeOfComeLoadingaAndNumSamosval(Timestamp date, Integer num_samosval){
        return objectRespository.findByTimeOfComeLoadingaAndNumSamosval(date, num_samosval);
    }

    public int getCountSamosvalByTimeAndType(Timestamp date, String type){
        return objectRespository.findCountSamosvalbyDateAndType(date, type).size();
    }

    public List<Integer> getNums(Timestamp date){
        return objectRespository.findNums(date);
    }

    public int getMaxReise(Timestamp date, Integer num_samosval){
        return objectRespository.getMaxReise(date, num_samosval);
    }

    public List<Object> getBytimeAndType(String type, Timestamp time){
        return objectRespository.findObjectByTypeOfWorkAndTime(type,time);
    }


    public List<String> getAllTypeOfWork(){
        return objectRespository.findAllTypeOfWork();
    }

    public int getCountEx(Timestamp date, String ex) {
        return objectRespository.findCountEx(date, ex).size();
    }

    public int getReises(Timestamp time){
        return objectRespository.findbyReise(time).size();
    }

    public List<String> getEx(Timestamp timestamp){
        return objectRespository.AllNameExByDay(timestamp);
    }

    public List<Object> getObjectsForDayByEx(Timestamp timestamp, String name){
        return objectRespository.findAllObjectByEx(timestamp, name);
    }

    public List<String> getTypesOfSamByexForDay(Timestamp timestamp, String name){
        return objectRespository.findAllTypeSamsvalByNameEx(timestamp, name);
    }

    public int getQuantityTruckFromExForDay(Timestamp timestamp, String name, String type){
        return objectRespository.findQuantitySamByEx(timestamp,name,type).size();
    }

    public List<String> getAllTypeExs(){
       return objectRespository.getAllTypeExs();
    }

    public List<String> getAllTypeSamosval(){
        return objectRespository.getAllTypeSamosval();
    }


    //optimize
    public List<String[]> getMonth(){
       return objectRespository.getMonth();
    }

    public List<String[]> getMonthByTypeOfWork(String type){
        return objectRespository.getMonthByTypeOfWork(type);
    }

    public List<String[]> getByExesByDay(Timestamp time){
        return objectRespository.getByExesByDay(time);
    }

    public List<String[]> getBySumosvalByDay(Timestamp time){
        return objectRespository.getBySumosvalByDay(time);
    }

    public List<String[]> getDataByDayByNumsum(Timestamp time, int num){
        return objectRespository.getDataByDayByNumsum(time, num);
    }

    public List<Object> getObjectsByDateAndByTypeTruck(Timestamp time, String typeTruck){
        return objectRespository.getObjectsByDateAndByTypeTruck(time,typeTruck);
    }
}
