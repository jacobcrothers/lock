package binar.box.repository;

import binar.box.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
