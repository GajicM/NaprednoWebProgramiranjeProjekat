package rs.raf.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IService<User,Long>, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public <S extends User> S save(S var1) {
        var1.setPassword(BCrypt.hashpw(var1.getPassword(),BCrypt.gensalt()));
        return userRepository.save(var1);
    }

    @Override
    public Optional<User> findById(Long var1) {
        return userRepository.findById(var1);
    }

    @Override
    public List<User> findAll() {
        System.out.println(userRepository.findAll());
        return (List<User>) userRepository.findAll();
    }

    @Override
    public void deleteByIdAndUserId(Long var1) {
        userRepository.deleteById(var1);
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User findByEmailAndPassword(String email, String password) {

         User u=userRepository.findUserByEmail(email);
         if(u!=null){
             if(comparePassword(password,u.getPassword())){
                 return u;
             }
         }
         return null;
    }
    private boolean comparePassword(String rawpw, String hashed){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        encoder.matches(rawpw, hashed);
        return (encoder.matches(rawpw, hashed));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User myUser = this.userRepository.findUserByEmail(email);

        System.out.println(myUser + " OVDEOVDOEOFOAODAJWDIOAWDOAWID" );
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+email+" not found");
        }

        return new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPassword(), new ArrayList<>());
    }
}
