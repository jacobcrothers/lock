package binar.box.repository;

import binar.box.domain.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Timis Nicu Alexandru on 02-Apr-18.
 */
@Repository
public interface AuthorityRepository extends JpaRepository<UserAuthority, Long> {
    List<UserAuthority> findByName(String name);
}
