package kg.parser.blastmaker.xlms.controller;

import kg.parser.blastmaker.xlms.report.OriginalData;
import kg.parser.blastmaker.xlms.model.PerReice;
import kg.parser.blastmaker.xlms.objects.Object;
import kg.parser.blastmaker.xlms.service.Parser;
import kg.parser.blastmaker.xlms.objects.TruckDTO;
import kg.parser.blastmaker.xlms.service.ObjectService;
import kg.parser.blastmaker.xlms.service.TruckService;
//import kg.parser.blastmaker.xlms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GreetingController {

    @Autowired
     ObjectService objectService;

    @Autowired
    TruckService truckService;

    @Autowired
    OriginalData calculation;

    @GetMapping({"/home", "/"})
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/calc/type")
    public String calc(Model model){
        model.addAttribute("types", objectService.getAllTypeOfWork());
        model.addAttribute("catsAndItems", calculation.month(""));
        return "Calc";
    }

    @GetMapping("/parse")
    public String submit() {
        Parser parser = new Parser("src/main/resources/text.txt");
        List<Object> objects = parser.getObjects();
        objectService.saveAlL(objects);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Object> objects = objectService.getAll();
        model.addAttribute("objects", objects);

        return "List";
    }

    @GetMapping("/trucks")
    public String truck( Model model){
        List<TruckDTO> truckDTOS = truckService.findAll();
        model.addAttribute("trucks", truckDTOS);
        return "Trucks";
    }

    @GetMapping("/waste")
    public String waste(Model model){
        List<TruckDTO> truckDTOS = truckService.findAll();
        model.addAttribute("truks", truckDTOS);
        return "waste";
    }

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
        model.addAttribute("types", objectService.getAllTypeOfWork());
        model.addAttribute("catsAndItems", calculation.month(type));
        return "Calc";
    }

}
