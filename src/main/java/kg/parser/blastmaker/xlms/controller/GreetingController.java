package kg.parser.blastmaker.xlms.controller;

import kg.parser.blastmaker.xlms.objects.TruckTripsDTO;
import kg.parser.blastmaker.xlms.report.OriginalData;
import kg.parser.blastmaker.xlms.model.PerReice;
import kg.parser.blastmaker.xlms.respositiry.*;
import kg.parser.blastmaker.xlms.service.Parser;
import kg.parser.blastmaker.xlms.objects.TruckTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GreetingController {

    @Autowired
    TruckTripsRespository truckTripsRespository;

    @Autowired
    ExcavatorDriverRepository excavatorDriverRepository;

    @Autowired
    ExcavatorRepository excavatorRepository;

    @Autowired
    TruckDriverRepository truckDriverRepository;

    @Autowired
    TruckTypeRepository truckTypeRepository;

    @Autowired
    TruckRepository truckRepository;

    @Autowired
    TypeOfWorkRepository typeOfWorkRepository;

    @Autowired
    UnloadPointRepository unloadPointRepository;

    @Autowired
    TruckTripsRespository objectService;

    @Autowired
    Parser parser;

    @Autowired
    OriginalData calculation;

    @GetMapping({"/home", "/"})
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/calc/type")
    public String calc(Model model){
        model.addAttribute("types", typeOfWorkRepository.getAllTypeOfWork());
        model.addAttribute("catsAndItems", calculation.month(""));
        return "Calc";
    }

    @GetMapping("/parse")
    public String submit() {
        parser.parse("src/main/resources/text.txt");
        List<TruckTripsDTO> truckTripsDTOS = parser.getTruckTripsDTOS();
        objectService.saveAll(truckTripsDTOS);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<TruckTripsDTO> truckTripsDTOS = objectService.findAll();
        model.addAttribute("objects", truckTripsDTOS);

        return "List";
    }

    @GetMapping("/trucks")
    public String truck( Model model){
        List<TruckTypeDTO> truckTypeDTOS = truckTypeRepository.findAll();
        model.addAttribute("trucks", truckTypeDTOS);
        return "Trucks";
    }

//    @GetMapping("/waste")
//    public String waste(Model model){
//        List<TruckTypeDTO> truckTypeDTOS = truckService.findAll();
//        model.addAttribute("truks", truckTypeDTOS);
//        return "waste";
//    }

    @GetMapping("calc/perdayByTruck/{day}")
    String filtByNum(Model model,@PathVariable(value = "day") int day){
        model.addAttribute("items", calculation.perDayByTruck(day));
        model.addAttribute("day", day);
        return "forDays\\perDayByTruck";
    }

    @GetMapping("calc/perdayByEx/{day}")
    String filtByEx(Model model,@PathVariable(value = "day") int day){
        model.addAttribute("items", calculation.perDayByExes(day));
        model.addAttribute("day", day);
        return "forDays\\perDayByExes";
    }

    @GetMapping("calc/day/{day}/num/{num}")
    String perReise(Model model,@PathVariable(value = "day") int day, @PathVariable(value = "num")Integer num){
        List<PerReice> objects = calculation.perReice(day,num);
        model.addAttribute("items", objects);
        return "forDays\\perReise";
    }

    @GetMapping("calc/type/{type}")
    String sortByType(Model model, @PathVariable(value = "type") String type){
        model.addAttribute("types", typeOfWorkRepository.getAllTypeOfWork());
        model.addAttribute("catsAndItems", calculation.month(type));
        return "Calc";
    }

}
