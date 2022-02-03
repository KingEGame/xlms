package kg.parser.blastmaker.xlms.controller;

import kg.parser.blastmaker.xlms.objects.Excavator;
import kg.parser.blastmaker.xlms.service.ExcavatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ExcavatorController {

    @Autowired
    ExcavatorService excavatorService;

    @GetMapping("ex/add")
    String reviewAdd(Model model){
        model.addAttribute("ex", new Excavator());
        return "AddEx";
    }

    @PostMapping("/ex/add")
    String add(Model model, @ModelAttribute Excavator ex){
        excavatorService.save(ex);
        return "redirect:/";
    }
}
