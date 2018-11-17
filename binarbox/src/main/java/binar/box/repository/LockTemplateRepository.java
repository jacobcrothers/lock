package binar.box.repository;

import binar.box.domain.LockTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface LockTemplateRepository extends BaseJpaRepository<LockTemplate, Long> {

}
