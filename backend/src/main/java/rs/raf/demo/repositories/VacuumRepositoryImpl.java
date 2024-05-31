package rs.raf.demo.repositories;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.VacuumStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;
/*
@Repository
public abstract class VacuumRepositoryImpl implements VacuumRepository{
    @Override
    public void deleteVacuumById(Long id) {
        this.findById(id).ifPresent(vacuum -> {
            vacuum.setDeleted(true);
            this.save(vacuum);
        }
        );
    }
    public List<Vacuum> search(Optional<String> name, Optional<VacuumStatus> status, Optional<Date> firstDate, Optional<Date> secondDate){
        return this.findAllByNameContainsIgnoreCaseAndStatusAndCreatedAtIsBetween( name,  status,  firstDate, secondDate);
    }
}


 */