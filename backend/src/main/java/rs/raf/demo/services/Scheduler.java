package rs.raf.demo.services;

import org.springframework.stereotype.Component;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.ErrorMessageRepository;
import rs.raf.demo.repositories.VacuumRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class Scheduler {
    private final AsyncComp asyncComp;
    private final VacuumRepository vacuumRepository;
    private final ErrorMessageRepository errorMessageRepository;
    public Scheduler(AsyncComp asyncComp, VacuumRepository vacuumRepository, ErrorMessageRepository errorMessageRepository) {
        this.asyncComp = asyncComp;
        this.vacuumRepository = vacuumRepository;
        this.errorMessageRepository = errorMessageRepository;
    }
    public  StartVacuum startVacuumOnSchedule(long vacuumId, long userId){
        return new StartVacuum(vacuumId,userId);
    }
    public  StopVacuum stopVacuumOnSchedule(long vacuumId,long userId){
        return new StopVacuum(vacuumId,userId);
    }
    public  DischargeVacuum dischargeVacuumOnSchedule(long vacuumId,long userId){
        return new DischargeVacuum(vacuumId,userId);
    }
    private void createAndSaveErrorMessage(String message, long vacuumId, String operation,long userId){
        ErrorMessage errorMessage=new ErrorMessage();
        errorMessage.setMessage(message);
        errorMessage.setVacuumId(vacuumId);
        errorMessage.setOperation(operation);
        errorMessage.setUserId(userId);
        errorMessage.setDateTime(LocalDateTime.now());
        errorMessageRepository.save(errorMessage);
    }
    public  class StartVacuum implements Runnable{

        private final long vacuumId;
      private final long userId;
        public StartVacuum(long vacuumId,long userId) {
            this.vacuumId = vacuumId;
            this.userId= userId;

        }

        @Override
        public void run() {
            Optional<Vacuum> v=    vacuumRepository.findById(vacuumId);
            if(!v.isPresent()){
                System.out.println("Vacuum not found");
               createAndSaveErrorMessage("Vacuum not found",vacuumId,"start",userId);
                return;
            }

            Vacuum vacuum=v.get();
            if(!vacuum.getAddedBy().getId().equals(userId)){
                System.out.println("User is not owner of vacuum");
                createAndSaveErrorMessage("User is not owner of vacuum",vacuumId,"start",userId);
                return;
            }
            if(vacuum.getStatus().equals(VacuumStatus.ON)) {
                System.out.println("Vacuum is already on");
                createAndSaveErrorMessage("Vacuum is already on",vacuumId,"start",userId);
                return;
            }
            if (vacuum.isLocked()) {
                System.out.println("Vacuum is locked");
                createAndSaveErrorMessage("Vacuum is locked",vacuumId,"start",userId);
                return;
            }
            if(!vacuum.isActive()){
                System.out.println("Vacuum is not active");
                createAndSaveErrorMessage("Vacuum is not active",vacuumId,"start",userId);
                return;
            }
      /*      vacuum.setLocked(true);
            vacuumRepository.save(vacuum);*/
            asyncComp.startVacuumAsync(vacuum);

        }
    }

    public  class StopVacuum implements Runnable{

        private final long vacuumId;
        private final long userId;

        public StopVacuum(long vacuumId,long userId) {
            this.vacuumId = vacuumId;
            this.userId= userId;
        }
        @Override
        public void run() {
            Optional<Vacuum> v=    vacuumRepository.findById(vacuumId);
            if(!v.isPresent()){
                System.out.println("Vacuum not found");
                createAndSaveErrorMessage("Vacuum not found",vacuumId,"stop",userId);
                return;
            }
            Vacuum vacuum=v.get();
            if(!vacuum.getAddedBy().getId().equals(userId)){
                System.out.println("User is not owner of vacuum");
                createAndSaveErrorMessage("User is not owner of vacuum",vacuumId,"start",userId);
                return;
            }
            if(vacuum.getStatus().equals(VacuumStatus.OFF)) {
                System.out.println("Vacuum is already off");
                createAndSaveErrorMessage("Vacuum is already off",vacuumId,"stop",userId);
                return;
            }
            if (vacuum.isLocked()) {
                System.out.println("Vacuum is locked");
                createAndSaveErrorMessage("Vacuum is locked",vacuumId,"stop",userId);
                return;
            }
            if(!vacuum.isActive()){
                System.out.println("Vacuum is not active");
                createAndSaveErrorMessage("Vacuum is not active",vacuumId,"stop",userId);
                return;
            }
         /*   vacuum.setLocked(true);
            vacuumRepository.save(vacuum);*/
            asyncComp.stopVacuumAsync(vacuum);

        }
    }

    public  class DischargeVacuum implements Runnable{

        private final long vacuumId;
        private final long userId;
        public DischargeVacuum(long vacuumId,long userId) {
            this.vacuumId = vacuumId;
            this.userId= userId;

        }

        @Override
        public void run() {
            Optional<Vacuum> v=    vacuumRepository.findById(vacuumId);
            if(!v.isPresent()){
                System.out.println("Vacuum not found");
                createAndSaveErrorMessage("Vacuum not found",vacuumId,"discharge",userId);
                return; //TODO ERROR
            }
            Vacuum vacuum=v.get();
            if(!vacuum.getAddedBy().getId().equals(userId)){
                System.out.println("User is not owner of vacuum");
                createAndSaveErrorMessage("User is not owner of vacuum",vacuumId,"start",userId);
                return;
            }
            if(vacuum.getStatus().equals(VacuumStatus.ON) || vacuum.getStatus().equals(VacuumStatus.DISCHARGING)) {
                System.out.println("Vacuum is on or already discharging");
                createAndSaveErrorMessage("Vacuum is on or already discharging",vacuumId,"discharge",userId);
                return; //TODO ERROR
            }
            if (vacuum.isLocked()) {
                System.out.println("Vacuum is locked");
                createAndSaveErrorMessage("Vacuum is locked",vacuumId,"discharge",userId);
                return; //TODO ERROR
            }
            if(!vacuum.isActive()){
                System.out.println("Vacuum is not active");
                createAndSaveErrorMessage("Vacuum is not active",vacuumId,"discharge",userId);
                return; //TODO ERROR
            }
           /* vacuum.setLocked(true);
            vacuumRepository.save(vacuum);*/
            asyncComp.dischargeVacuumAsync(vacuum);

        }
    }
}
