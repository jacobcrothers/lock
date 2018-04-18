package binar.box.repository;

import binar.box.domain.Lock;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public interface LockRepository extends JpaRepository<Lock, Long> {
}
