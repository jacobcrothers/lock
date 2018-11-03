package binar.box.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import binar.box.domain.LockType;

public interface LockTypeRepository extends JpaRepository<LockType, Long> {
}
