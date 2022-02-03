//package kg.parser.blastmaker.xlms.controller.authorithation;
//
//import kg.parser.blastmaker.xlms.objects.Role;
//import kg.parser.blastmaker.xlms.objects.User;
//import kg.parser.blastmaker.xlms.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.util.Collections;
//import java.util.List;
//
//@Controller
//public class RegistrationController {
//
//    @Autowired
//    private UserService userService;
//
//
//    @GetMapping("/registration")
//    public String registration() {
//        return "registration";
//    }
//
//    @PostMapping("/registration")
//    public String addUser(User user, Model model) {
//
//        userService.save(User.builder().active(true).name("admin").roles(Collections.singleton(Role.Dispatcher)).password("admin").build());
//
//
//        User users = userService.findByUserName(user.getName());
//
//
//        if(users != null) {
//            model.addAttribute("message", "User exists!");
//            return "registration";
//        }
//
//        user.setActive(true);
//        user.setRoles(Collections.singleton(Role.Driver));
//        userService.save(user);
//
//        return "redirect:/login";
//    }
//}
