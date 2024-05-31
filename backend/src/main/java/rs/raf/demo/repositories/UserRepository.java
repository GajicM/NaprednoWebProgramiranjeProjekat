package rs.raf.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.raf.demo.model.User;


public interface UserRepository extends CrudRepository<User, Long> {
   User findUserByEmail(String email);
   User findUserByEmailAndPassword(String email, String password);

}