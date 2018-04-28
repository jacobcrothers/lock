package binar.box.repository;

import binar.box.domain.Lock;
import binar.box.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public interface LockRepository extends JpaRepository<Lock, Long> {

    List<Lock> findByUser(User user);
}
