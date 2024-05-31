package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.VacuumStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VacuumRepository extends JpaRepository<Vacuum, Long> {

    Vacuum findByIdAndAddedById(Long id, Long userId);

    @Query("SELECT v FROM Vacuum v " +
            "WHERE (:userId =v.addedBy.id)" +
            "AND (:name IS NULL OR LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:statusIsNull is null or  v.status in :status)" +
            "AND (v.active is true)" +
            " AND  (:firstDate is NUll OR v.createdAt >= :firstDate) " +
            "AND (:secondDate IS NULL OR v.createdAt BETWEEN :firstDate AND :secondDate)")
    List<Vacuum> findAllByNameContainsIgnoreCaseAndStatusAndCreatedAtIsBetween(@Param("userId") long userId,
                                                                               @Param("name") Optional<String> name,
                                                                               @Param("statusIsNull")boolean statusIsNull,
                                                                               @Param("status") Optional<List<VacuumStatus>> status,
                                                                               @Param("firstDate") Optional<Date> firstDate,
                                                                               @Param("secondDate") Optional<Date> secondDate);

    default List<Vacuum> search(long userId,Optional<String> name, Optional<List<VacuumStatus>> status, Optional<Date> firstDate, Optional<Date> secondDate){
           boolean statusIsNull=false;
            if(!status.isPresent() || status.get().isEmpty())
                statusIsNull=true;
        return this.findAllByNameContainsIgnoreCaseAndStatusAndCreatedAtIsBetween(userId, name,statusIsNull,  status , firstDate, secondDate);
    }



    default void startVacuum(Long id) {
        this.findById(id).ifPresent(vacuum -> {
                    vacuum.setStatus(VacuumStatus.ON);
                    this.save(vacuum);
                }
        );}

}
