package binar.box.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import binar.box.domain.File;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public interface FileRepository extends JpaRepository<File, Long> {
}
