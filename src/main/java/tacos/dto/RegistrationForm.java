package tacos.dto;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.entity.User;

@Data
public class RegistrationForm {

    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;

    public User toUser(PasswordEncoder passwordEncoder){
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .fullname(fullname)
                .street(street)
                .city(city)
                .state(state)
                .zip(zip)
                .phoneNumber(phone)
                .build();
    }
}
