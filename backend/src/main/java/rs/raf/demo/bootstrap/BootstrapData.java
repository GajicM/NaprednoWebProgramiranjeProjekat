package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumRepository;


import java.util.*;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository studentRepository;
    @Autowired
    private final VacuumRepository vacuumRepository;


    @Autowired
    public BootstrapData(UserRepository studentRepository, VacuumRepository vacuumRepository) {
        this.studentRepository = studentRepository;
        this.vacuumRepository = vacuumRepository;

    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        String[] FIRST_NAME_LIST = {"John-James", "Justine", "Ahsan", "Leja", "Jad", "Vernon", "Cara", "Eddison", "Eira", "Emily"};
        String[] LAST_NAME_LIST = {"Booker", "Summers", "Reyes", "Rahman", "Crane", "Cairns", "Hebert", "Bradshaw", "Shannon", "Phillips"};

        Random random = new Random();

        List<User> teachers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User teacher = new User();
            teacher.setFirstName(FIRST_NAME_LIST[random.nextInt(FIRST_NAME_LIST.length)]);
            teacher.setLastName(LAST_NAME_LIST[random.nextInt(LAST_NAME_LIST.length)]);
            teacher.setEmail(teacher.getFirstName()+"."+teacher.getLastName()+"@raf.rs");
            teachers.add(teacher);
            HashSet<Permission> permissions = new HashSet<>();
            permissions.add(Permission.can_create_users);
            teacher.setPermissions( permissions);
            teacher.setPassword(BCrypt.hashpw("password",BCrypt.gensalt()));
        }
        System.out.println();
        User teacher = new User();
        teacher.setFirstName("milos");
        teacher.setLastName("gajic");
        teacher.setEmail("milos.gajic2000@gmail.com");
        teacher.setPassword(BCrypt.hashpw("password",BCrypt.gensalt()));
        HashSet<Permission> permissions = new HashSet<>();
        permissions.add(Permission.can_create_users);
        permissions.add(Permission.can_delete_users);
        permissions.add(Permission.can_update_users);
        permissions.add(Permission.can_read_users);

        permissions.add(Permission.can_add_vacuum);
        permissions.add(Permission.can_remove_vacuum);
        permissions.add(Permission.can_search_vacuum);
        permissions.add(Permission.can_start_vacuum);
        permissions.add(Permission.can_stop_vacuum);
        permissions.add(Permission.can_discharge_vacuum);

        teacher.setPermissions(permissions);
        studentRepository.saveAll(teachers);
        studentRepository.save(teacher); System.out.println(teacher.getEmail()+teacher.getPermissions());
        teachers.add(teacher);
        System.out.println(studentRepository.findUserByEmail("milos.gajic2000@gmail.com"));
        List<Vacuum> vacuumList= new ArrayList<>();
        for(int i=0;i<10;i++){
            Vacuum vacuum = new Vacuum();
            vacuum.setName("Vacuum"+i);
            vacuum.setActive(true);
            vacuum.setStatus(VacuumStatus.OFF);
            vacuum.setCycles(0);
            vacuum.setAddedBy(teachers.get(random.nextInt(teachers.size())));
            vacuumList.add(vacuum);
        }
        vacuumRepository.saveAll(vacuumList);


        System.out.println("Data loaded!");
    }
}
