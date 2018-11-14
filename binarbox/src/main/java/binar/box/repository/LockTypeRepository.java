package binar.box.repository;

import binar.box.domain.LockCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LockTypeRepository extends BaseJpaRepository<LockCategory, Long> {

    @Query(value = "SELECT * FROM lock_category lt \n" +
            "INNER JOIN lock_type_template ltt ON ltt.lock_category=lt.id \n" +
            "INNER JOIN file f ON f.lock_category=lt.id \n" +
            "INNER JOIN price p ON lt.price=p.id \n" +
            "WHERE lt.id=:id", nativeQuery = true)
    Optional<LockCategory> findByIdWithTemplatePriceAndFile(@Param("id") Long id);
}
