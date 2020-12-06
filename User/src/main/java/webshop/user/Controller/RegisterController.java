package webshop.user.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @RequestMapping("/register")
    @ResponseBody
    public String register(@RequestParam String username, @RequestParam String pwd1, @RequestParam String pwd2, @RequestParam String firstname, @RequestParam String lastname) {
        //TODO: Implement
        System.out.println("Register called");
        return "";
    }
}
