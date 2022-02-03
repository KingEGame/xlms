package kg.parser.blastmaker.xlms.controller;

import kg.parser.blastmaker.xlms.objects.Truck;
import kg.parser.blastmaker.xlms.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TruckController {

    @Autowired
    TruckService truckService;

    @GetMapping("/truck/add")
    String reviewAdd(Model model){
        model.addAttribute("truck", new Truck());
        return "AddTruck";
    }

    @PostMapping("/truck/add")
    String add(Model model, @ModelAttribute Truck truck){
        truckService.save(truck);
        return "redirect:/";
    }
}
