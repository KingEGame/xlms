package kg.parser.blastmaker.xlms.controller;

import kg.parser.blastmaker.xlms.objects.Object;
import kg.parser.blastmaker.xlms.objects.Parser;
import kg.parser.blastmaker.xlms.objects.Truck;
import kg.parser.blastmaker.xlms.respositiry.ObjectRespository;
import kg.parser.blastmaker.xlms.service.ObjectService;
import kg.parser.blastmaker.xlms.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GreetingController {

    @Autowired
     ObjectService objectService;

    @Autowired
    TruckService truckService;

    @Autowired
    ObjectRespository objectRespository;

    @GetMapping({"/home", " "})
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/parse")
    public String submit(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        Parser parser = new Parser("src/main/resources/text.txt");
        List<Object> objects = parser.getObjects();

        objectService.saveAlL(objects);
        model.addAttribute("objects", objectService.getAll());

        return "List";
    }

    @GetMapping("/list")
    public String list(@RequestParam(name="name", required=false, defaultValue="list") String name, Model model) {
        List<Object> objects = objectService.getAll();
        model.addAttribute("objects", objects);

        return "List";
    }

    @GetMapping("/optimize")
    public String optimize(@RequestParam(name = "name", required = false, defaultValue = "oprimize") String name, Model model){
        model.addAttribute("name", name);
        return "optimize";
    }

    @GetMapping("/trucks")
    public String truck(@RequestParam(name = "name", required = false, defaultValue = "truck") String name, Model model){
        List<Truck> trucks = truckService.findAll();
        model.addAttribute("trucks",trucks );
        return "Trucks";
    }

    @GetMapping("/waste")
    public String waste(@RequestParam(name = "name", required = false, defaultValue = "waste") String name, Model model){
        List<Truck> trucks = truckService.findAll();
        model.addAttribute("truks", trucks);
        return "waste";
    }

}
