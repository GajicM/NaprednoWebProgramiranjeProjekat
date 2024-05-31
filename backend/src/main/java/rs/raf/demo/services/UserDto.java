package rs.raf.demo.services;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter@Setter
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String token;
    //public Set<Permission> permission;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
             //   ", permission=" + permission +
                '}';
    }
}
