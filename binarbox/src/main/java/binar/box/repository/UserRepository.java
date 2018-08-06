package binar.box.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import binar.box.domain.User;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmail(String email);

}
