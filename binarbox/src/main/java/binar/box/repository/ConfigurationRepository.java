package binar.box.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import binar.box.domain.Configuration;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

}
