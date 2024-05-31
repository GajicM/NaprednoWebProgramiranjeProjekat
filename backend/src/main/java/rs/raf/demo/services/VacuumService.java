package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumRepository;
import rs.raf.demo.utils.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VacuumService {

    @Autowired
    private final VacuumRepository vacuumRepository;
    private final AsyncComp asyncComp;
    private final TaskScheduler taskScheduler;
    private final Scheduler scheduler;
    private final UserRepository userRepository;


    public VacuumService(VacuumRepository vacuumRepository, AsyncComp asyncComp, TaskScheduler taskScheduler, Scheduler scheduler, UserRepository userRepository ) {
        this.vacuumRepository = vacuumRepository;
        this.asyncComp=asyncComp;
        this.taskScheduler = taskScheduler;
        this.scheduler = scheduler;
        this.userRepository = userRepository;

    }



    public Vacuum save(String name,long userId) {
        Vacuum var1 = new Vacuum();
        userRepository.findById(userId).ifPresent(var1::setAddedBy);
        var1.setStatus(VacuumStatus.OFF);
        var1.setActive(true);
        var1.setName(name);
        var1.setCycles(0);
        var1.setLocked(false);

        return vacuumRepository.save(var1);
    }

    public Optional<Vacuum> findById(Long var1) {
        return vacuumRepository.findById(var1);
    }


    @Transactional
    public void deleteByIdAndUserId(Long var1,long userId) {

       Vacuum v= vacuumRepository.findByIdAndAddedById(var1,userId);
       v.setActive(false);
         vacuumRepository.save(v);
    }

    public List<Vacuum> search(long userId,Optional<String> name, Optional<List<VacuumStatus>> status, Optional<String> firstDate, Optional<String> secondDate){

        return vacuumRepository.search(userId,name,status,
                Optional.ofNullable(DateUtil.parseDate2(firstDate.orElseGet(() -> null))),
                Optional.ofNullable(DateUtil.parseDate2(secondDate.orElse(null))));

    }
    public ResponseEntity<?> startVacuum(Long id,long userId) {
     Optional<Vacuum> v=   vacuumRepository.findById(id);
        if(!v.isPresent()){
            return ResponseEntity.status(404).body("Vacuum not found");
        }
        Vacuum vacuum=v.get();
        if(!vacuum.getAddedBy().getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not the owner of this vacuum");
        }
       /* if(vacuum.getStatus().equals(VacuumStatus.ON)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vacuum is already on");
        }
        if(vacuum.isLocked()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vacuum is already working");
        }
       vacuum.setLocked(true);
        vacuumRepository.save(vacuum);*/

        asyncComp.startVacuumAsync(vacuum);
        return ResponseEntity.ok().build();





    }
    public ResponseEntity<?> stopVacuum(Long id,long userId) {
        Optional<Vacuum> v=   vacuumRepository.findById(id);
        if(!v.isPresent()){
            return ResponseEntity.status(404).body("Vacuum not found");
        }
        Vacuum vacuum=v.get();
   /*     if(vacuum.getStatus().equals(VacuumStatus.OFF)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vacuum is already off");
        }*/
        if(!vacuum.getAddedBy().getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not the owner of this vacuum");
        }
    /*    if(vacuum.isLocked()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vacuum is already working");
        }
        vacuum.setLocked(true);
        vacuumRepository.save(vacuum);*/
        asyncComp.stopVacuumAsync(vacuum);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<?> dischargeVacuum(Long id,long userId) {
        Optional<Vacuum> v=   vacuumRepository.findById(id);
        if(!v.isPresent()){
            return ResponseEntity.status(404).body("Vacuum not found");
        }
        Vacuum vacuum=v.get();
        if(!vacuum.getAddedBy().getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not the owner of this vacuum");
        }
        if(vacuum.getStatus().equals(VacuumStatus.ON)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vacuum is already on");
        }
        if(vacuum.getStatus().equals(VacuumStatus.DISCHARGING)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vacuum is already discharging");
        }
        if(vacuum.isLocked()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vacuum is already working");
        }
        vacuum.setLocked(true);
        vacuumRepository.save(vacuum);
        asyncComp.dischargeVacuumAsync(vacuum);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> scheduleStart(Long id,Date date,Long userId){
        System.out.println(date);
        System.out.println(taskScheduler.getClock());
        taskScheduler.schedule(this.scheduler.startVacuumOnSchedule(id,userId),date);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> scheduleDischarge(Long id, Date date, Long userId) {
        taskScheduler.schedule(this.scheduler.dischargeVacuumOnSchedule(id,userId),date);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> scheduleStop(Long id, Date date, Long userId) {
        taskScheduler.schedule(this.scheduler.stopVacuumOnSchedule(id,userId),date);
        return ResponseEntity.ok().build();
    }
}
