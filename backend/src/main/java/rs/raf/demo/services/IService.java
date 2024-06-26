package rs.raf.demo.services;

import java.util.List;
import java.util.Optional;

public interface IService<T, ID> {
    <S extends T> S save(S var1);

    Optional<T> findById(ID var1);

    List<T> findAll();

    void deleteByIdAndUserId(ID var1);

}
