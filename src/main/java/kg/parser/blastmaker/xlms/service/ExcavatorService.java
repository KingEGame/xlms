package kg.parser.blastmaker.xlms.service;

import kg.parser.blastmaker.xlms.objects.Excavator;
import kg.parser.blastmaker.xlms.respositiry.ExcavatorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcavatorService {

    @Autowired
    ExcavatorRepo excavatorRepo;

    public Excavator save(Excavator excavator){
        return excavatorRepo.saveAndFlush(excavator);
    }

    public void delete(long id){
        excavatorRepo.deleteById(id);
    }

    public Excavator getById(long id){
        return excavatorRepo.getById(id);
    }

    public Excavator getByName(String name){
        return excavatorRepo.getByName(name).get(0);
    }
}
