package webshop.user.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String firstname;

    public User(String username, String password, String firstname) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
    }

    public User() {
    }

    public Boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }
}
