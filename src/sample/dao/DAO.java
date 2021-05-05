package sample.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    Optional<T> get(int id);
    List<T> getAll();
    boolean save(T t);
    void update(int id, T t);
    void delete(int id);
}
