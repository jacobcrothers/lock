package binar.box.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import binar.box.domain.LockTypeTemplate;

@Repository
public interface LockTypeTemplateRepository extends JpaRepository<LockTypeTemplate, Long> {

}
