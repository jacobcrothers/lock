package binar.box.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import binar.box.domain.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
