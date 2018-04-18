package binar.box.repository;

import binar.box.domain.LockType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public interface LockTypeRepository extends JpaRepository<LockType, Long> {
}
