package kg.parser.blastmaker.xlms.service;

import kg.parser.blastmaker.xlms.objects.Object;
import kg.parser.blastmaker.xlms.objects.Truck;
import kg.parser.blastmaker.xlms.respositiry.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckService {

    @Autowired
    TruckRepository truckRepository;

    public Truck save(Truck payment){
        return truckRepository.saveAndFlush(payment);
    }

    public List<Truck> saveAll(List<Truck> trucks){
        return  truckRepository.saveAll(trucks);
    }

    public List<Truck> findAll(){
        return  truckRepository.findAll();
    }

    public Truck getById(Long id){
        return truckRepository.getById(id);
    }

    public Truck getByName(String name){
        if(truckRepository.getByName(name).size() == 0){
            System.out.println("dfgsg");
        }
        return truckRepository.getByName(name).get(0);
    }
}
