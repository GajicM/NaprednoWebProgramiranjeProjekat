package rs.raf.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.security.CheckPermission;
import rs.raf.demo.services.UserService;


import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final UserService userService;



    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/")
    @CheckPermission(value = {Permission.can_read_users})
    public ResponseEntity<Iterable<User>> getUsers(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(this.userService.findAll());
    }


    @GetMapping(path = "getUser/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CheckPermission(value = {Permission.can_read_users})
    public ResponseEntity<?> getStudentById(@RequestHeader("Authorization") String authorization, @PathVariable Long id){

        Optional<User> optionalStudent = userService.findById(id);
        if(optionalStudent.isPresent()) {
            return ResponseEntity.ok(optionalStudent.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping(path = "/addUser",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CheckPermission(value = {Permission.can_create_users})
    public ResponseEntity<?> createStudent(@RequestHeader("Authorization") String authorization, @RequestBody User user){
        return ResponseEntity.ok(userService.save(user));
    }

    @PutMapping(path = "/updateUser",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CheckPermission(value = {Permission.can_update_users})
    public User updateStudent(@RequestHeader("Authorization") String authorization ,@RequestBody User user){
        return userService.save(user);
    }



    @CheckPermission(value = {Permission.can_delete_users})
    @DeleteMapping(value = "deleteUsers/{id}")
    public ResponseEntity<?> deleteStudent(@RequestHeader("Authorization") String authorization ,@PathVariable Long id){
        userService.deleteByIdAndUserId(id);
        return ResponseEntity.noContent().build();
    }

}
