package binar.box.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import binar.box.domain.LockType;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public interface LockTypeRepository extends JpaRepository<LockType, Long> {
}
