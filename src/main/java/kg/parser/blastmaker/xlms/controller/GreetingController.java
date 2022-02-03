package kg.parser.blastmaker.xlms.controller;

import kg.parser.blastmaker.xlms.nowaday.Calculation;
import kg.parser.blastmaker.xlms.nowaday.PerReise;
import kg.parser.blastmaker.xlms.objects.Object;
import kg.parser.blastmaker.xlms.objects.Parser;
import kg.parser.blastmaker.xlms.objects.Truck;
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

//    @Autowired
//    UserService userService;

    @Autowired
     ObjectService objectService;

    @Autowired
    TruckService truckService;

    @Autowired
    Calculation calculation;

    @GetMapping({"/home", "/"})
    public String viewHomePage() {
        return "index";
    }


    @GetMapping("/calc/type")
    public String calc(Model model){
        model.addAttribute("types", objectService.getAllTypeOfWork());
        model.addAttribute("catsAndItems", calculation.time(""));
        return "Calc";
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
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

    @GetMapping("/optimize")
    public String optimize(){
        return "optimize";
    }

    @GetMapping("/trucks")
    public String truck( Model model){
        List<Truck> trucks = truckService.findAll();
        model.addAttribute("trucks",trucks );
        return "Trucks";
    }

    @GetMapping("/waste")
    public String waste(Model model){
        List<Truck> trucks = truckService.findAll();
        model.addAttribute("truks", trucks);
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
        List<PerReise> objects = calculation.perreise(day,num);
        model.addAttribute("items", objects);
        return "forDays\\perReise";
    }

    @GetMapping("calc/type/{type}")
    String sortByType(Model model, @PathVariable(value = "type") String type){
        model.addAttribute("types", objectService.getAllTypeOfWork());
        model.addAttribute("catsAndItems", calculation.time(type));
        return "Calc";
    }

}
