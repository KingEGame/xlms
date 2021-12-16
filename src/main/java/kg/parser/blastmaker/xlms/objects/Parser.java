package kg.parser.blastmaker.xlms.objects;

import kg.parser.blastmaker.xlms.service.ObjectService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Setter
@Getter
public class Parser {

    private List<Object> objects;

    public Parser(String path){
        InputStream ins = null;
        try {
            ins = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner obj = new Scanner(ins);
        objects = new ArrayList<>();
        while (obj.hasNextLine()){
            String[] strs = obj.nextLine().split("\t");

            Object object = Object.builder().
                    numSamosval(Integer.parseInt(strs[0])).
                    nameDriverSamosval(strs[1]).
                    typeSamosval(strs[2]).
                    reise(Integer.parseInt(strs[3])).
                    numEx(strs[4]).
                    nameEx(strs[5]).
                    timeOfComeLoading( Time.valueOf(strs[6])).
                    timeOfBeginLoading(Time.valueOf(strs[7])).
                    timeOfLoading( Time.valueOf(strs[8])).
                    distance(Double.parseDouble(strs[9].replace(",", "."))).
                    weightFact(Integer.parseInt(strs[10])).
                    weightNorm(Double.parseDouble(strs[11].replace(",", "."))).
                    typeOfWork(strs[12]).
                    placeToUnloading(strs[13]).
                    timeOfBeginUnloading(Time.valueOf(strs[14])).
                    timeOfUnloading( Time.valueOf(strs[15])).
                    gasForBeginLoading(Integer.parseInt(strs[16])).
                    gasForBeginUnloading(Integer.parseInt(strs[17])).
                    build();

            objects.add(object);
        }

    }
}