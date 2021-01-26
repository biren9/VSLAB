package webshop.authorization.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import webshop.authorization.Model.User;
import webshop.authorization.Repository.UserRepository;

import java.util.Optional;

@Service("userService")
public class WebshopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public WebshopUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        return userOpt.orElseThrow(() -> new UsernameNotFoundException("User with name '" + username + "' not found"));
    }

}