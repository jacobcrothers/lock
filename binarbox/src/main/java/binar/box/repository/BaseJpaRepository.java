package binar.box.repository;

import binar.box.util.Exceptions.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface BaseJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    default T findOne(ID id) {
        Optional<T> result = findById(id);
        return result.orElseThrow(() -> new EntityNotFoundException("Resource with id " + id + " not found", "entity.not.found", id));
    }

    default void deleteOne(ID id) {
        T result = findOne(id);
        delete(result);
    }
}
