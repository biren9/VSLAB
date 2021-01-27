package webshop.authorization.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class UserController {

    @PutMapping("/test")
    public ResponseEntity<Boolean> test() {
        return new ResponseEntity<Boolean>(HttpStatus.ACCEPTED);
    }

    @GetMapping("self")
    public Principal user(Principal principal) {
        return principal;
    }

}
