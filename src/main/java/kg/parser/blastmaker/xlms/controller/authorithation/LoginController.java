//package kg.parser.blastmaker.xlms.controller.authorithation;
//
//import kg.parser.blastmaker.xlms.objects.Role;
//import kg.parser.blastmaker.xlms.objects.User;
//import kg.parser.blastmaker.xlms.service.ObjectService;
//import kg.parser.blastmaker.xlms.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//public class LoginController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    ObjectService objectService;
//
//    @GetMapping("/login")
//    public String pagelogin(Model model, String name){
//        model.addAttribute("name", name);
//        return "login";
//    }
//
//    @RequestMapping(value="/login", method = RequestMethod.POST)
//    public String login(@ModelAttribute User user, Model model){
//
//       List<User> users = userService.findAll();
//
//        for(User user1 : users ){
//            if (user1.getName().equals(user.getName())) {
//                if(user.getPassword() != user1.getPassword()){
//                    model.addAttribute("message", "Password invalid");
//                    return "login";
//                }else{
//                    if (user1.getRoles().contains(Role.Driver)){
//                        return "driver";
//                    }
//                    return "redirect:/";
//                }
//            }
//        }
//
//        model.addAttribute("message", "Password invalid");
//        return "login";
//
//    }
//
//    @PostMapping("/logout")
//    public String logout(Model model){
//        model.addAttribute("name", "name");
//        return "login";
//    }
//}
