package kg.parser.blastmaker.xlms.service;

import kg.parser.blastmaker.xlms.objects.TruckDTO;
import kg.parser.blastmaker.xlms.respositiry.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckService {

    @Autowired
    TruckRepository truckRepository;

    public TruckDTO save(TruckDTO payment){
        return truckRepository.saveAndFlush(payment);
    }

    public List<TruckDTO> saveAll(List<TruckDTO> truckDTOS){
        return  truckRepository.saveAll(truckDTOS);
    }

    public List<TruckDTO> findAll(){
        return  truckRepository.findAll();
    }

    public TruckDTO getById(Long id){
        return truckRepository.getById(id);
    }

    public TruckDTO getByName(String name){
        return truckRepository.getByName(name).get(0);
    }
}
