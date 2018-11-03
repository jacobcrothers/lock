package binar.box.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import binar.box.domain.LockSection;

public interface LockSectionRepository extends JpaRepository<LockSection, Long> {

	List<LockSection> findBySection(char letter);
}
