package binar.box.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import binar.box.domain.UserAuthority;

/**
 * Created by Timis Nicu Alexandru on 02-Apr-18.
 */
@Repository
public interface AuthorityRepository extends JpaRepository<UserAuthority, Long> {
	List<UserAuthority> findByName(String name);
}
