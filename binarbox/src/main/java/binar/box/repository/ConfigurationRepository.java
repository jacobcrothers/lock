package binar.box.repository;

import org.springframework.stereotype.Repository;

import binar.box.domain.Configuration;

@Repository
public interface ConfigurationRepository extends BaseJpaRepository<Configuration, Long> {

}
