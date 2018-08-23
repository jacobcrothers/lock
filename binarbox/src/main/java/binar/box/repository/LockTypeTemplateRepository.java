package binar.box.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import binar.box.domain.LockTypeTemplate;

/**
 * Created by Timis Nicu Alexandru on 23-Aug-18.
 */
@Repository
public interface LockTypeTemplateRepository extends JpaRepository<LockTypeTemplate, Long> {

}
