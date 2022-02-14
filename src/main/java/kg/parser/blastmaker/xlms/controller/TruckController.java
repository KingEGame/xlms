package kg.parser.blastmaker.xlms.controller;

import kg.parser.blastmaker.xlms.objects.TruckDTO;
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
        model.addAttribute("truck", new TruckDTO());
        return "AddTruck";
    }

    @PostMapping("/truck/add")
    String add(Model model, @ModelAttribute TruckDTO truckDTO){
        truckService.save(truckDTO);
        return "redirect:/";
    }
}
