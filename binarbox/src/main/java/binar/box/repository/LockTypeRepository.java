package binar.box.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import binar.box.domain.LockType;

import java.util.Optional;

public interface LockTypeRepository extends JpaRepository<LockType, Long> {

//    @EntityGraph(value = "LockType.templateAndFiles", type = EntityGraph.EntityGraphType.LOAD)
    Optional<LockType> findById(Long id);
}
