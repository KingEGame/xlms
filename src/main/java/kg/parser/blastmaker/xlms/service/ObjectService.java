package kg.parser.blastmaker.xlms.service;

import kg.parser.blastmaker.xlms.objects.Object;
import kg.parser.blastmaker.xlms.respositiry.ObjectRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return objectRespository.findAll();
    }

//    public Object getByRequestId(Long requestId){
//        return objectRespository.getFirstByRequestId(requestId);
//    }
}
