package webshop.user.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webshop.user.Model.User;
import webshop.user.Repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-12-08T15:00:53.920Z")

@RestController
public class UserApiController {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @org.springframework.beans.factory.annotation.Autowired
    public UserApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PutMapping("/test")
    public ResponseEntity<Boolean> test() {
        return new ResponseEntity<Boolean>(HttpStatus.ACCEPTED);
    }


    @PutMapping(value = "/user/register")
    public ResponseEntity<Boolean> register() {
        String username = request.getHeader("username");
        String password1 = request.getHeader("password1");
        String password2 = request.getHeader("password2");
        String firstname = request.getHeader("firstname");

        if (username == null || password1 == null || password2 == null || firstname == null) {
            return new ResponseEntity<Boolean>(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            Boolean isUsernameAlreadyTaken = !userRepository.findByUsername(username).isEmpty();
            if (isUsernameAlreadyTaken) {
                return new ResponseEntity<Boolean>(objectMapper.readValue("false", Boolean.class), HttpStatus.NOT_ACCEPTABLE);
            }
            Boolean passwordEqual = password1.equals(password2);
            if (!passwordEqual) {
                return new ResponseEntity<Boolean>(objectMapper.readValue("false", Boolean.class), HttpStatus.NOT_ACCEPTABLE);
            }

            User newUser = new User(username, password1, firstname);
            userRepository.save(newUser);
            return new ResponseEntity<Boolean>(HttpStatus.ACCEPTED);
        } catch (IOException e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
