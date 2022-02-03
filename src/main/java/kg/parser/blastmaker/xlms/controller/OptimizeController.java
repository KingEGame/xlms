package kg.parser.blastmaker.xlms.controller;

import kg.parser.blastmaker.xlms.optimize.Optimize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OptimizeController {

    @Autowired
    Optimize optimize;

    @GetMapping("calc/perdayByEx/{day}/optimize")
    String optimize(Model model, @PathVariable(value = "day") int day){
       model.addAttribute("items", optimize.waste(day));
       model.addAttribute("day", day);

        return "forDays\\perDayByExes";
    }

    @GetMapping("/calc/optimize")
    String optimizeMonth(Model model){

        model.addAttribute("month", optimize.wasteMonth());
        return "CalcWaste";
    }
}
