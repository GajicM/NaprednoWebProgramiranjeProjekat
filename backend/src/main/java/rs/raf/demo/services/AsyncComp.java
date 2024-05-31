package rs.raf.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import rs.raf.demo.controllers.SseEmitterManager;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.VacuumRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class AsyncComp {
    @Autowired
    private VacuumRepository vacuumRepository;

  public AsyncComp(VacuumRepository vacuumRepository) {
        this.vacuumRepository = vacuumRepository;
    }

    @Async @Transactional
  public void  startVacuumAsync(Vacuum v){
        try{
            Thread.sleep(15000);
            System.out.println(v.getName() + " is started");
            v.setStatus(VacuumStatus.ON);
            v.setLocked(false);
            vacuumRepository.save(v);
            SseEmitterManager.sendSseEventToClients(v.getAddedBy().getId().toString(), v.getStatus());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ObjectOptimisticLockingFailureException e){
            System.out.println("Vacuum is locked");
            System.out.println("OPTIMISTIC LOCK");

        }

  }

    @Async
    public void  stopVacuumAsync(Vacuum v){
        try{
            Thread.sleep(15000);
            System.out.println(v.getName() + " stopped");
            v.setStatus(VacuumStatus.OFF);
            v.setCycles(v.getCycles()+1);
            v.setLocked(false);
            vacuumRepository.save(v);
            SseEmitterManager.sendSseEventToClients(v.getAddedBy().getId().toString(), v.getStatus());
            if (v.getCycles() == 3) {
             /*   v.setLocked(true);
                vacuumRepository.save(v);*/
                v.setVersion(v.getVersion()+1); //TODO: OVO JE NAHAKOVANO
                dischargeVacuumAsync(v);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ObjectOptimisticLockingFailureException e){

        System.out.println("Vacuum is locked");
        System.out.println("OPTIMISTIC LOCK");
    }
    }

    @Async
    public void dischargeVacuumAsync(Vacuum v){

      try{
          Thread.sleep(15000);
          v.setStatus(VacuumStatus.DISCHARGING);
          vacuumRepository.save(v);
          SseEmitterManager.sendSseEventToClients(v.getAddedBy().getId().toString(), v.getStatus());
            System.out.println(v.getName() + " is discharging");
            Thread.sleep(15000);
            System.out.println(v.getName() + " is discharged");
          v.setVersion(v.getVersion()+1); //TODO: OVO JE NAHAKOVANO
            v.setStatus(VacuumStatus.OFF);
            v.setCycles(0);
            v.setLocked(false);
            vacuumRepository.save(v);
          SseEmitterManager.sendSseEventToClients(v.getAddedBy().getId().toString(), v.getStatus());

      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }catch(ObjectOptimisticLockingFailureException e){
          stopVacuumAsync(v);
      }


    }


}
