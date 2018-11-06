package binar.box.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import binar.box.domain.LockType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LockTypeRepository extends JpaRepository<LockType, Long> {

    @Query(value = "SELECT * FROM lock_type lt \n" +
            "INNER JOIN lock_type_template ltt ON ltt.lock_type=lt.id \n" +
            "INNER JOIN file f ON f.lock_type=lt.id \n" +
            "INNER JOIN price p ON lt.price=p.id \n" +
            "WHERE lt.id=:id", nativeQuery = true)
    Optional<LockType> findByIdWithTemplatePriceAndFile(@Param("id") Long id);
}
