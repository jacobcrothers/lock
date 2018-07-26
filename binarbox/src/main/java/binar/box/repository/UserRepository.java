package binar.box.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import binar.box.domain.User;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByResetPasswordToken(String token);

	Optional<User> findByConfirmEmailToken(String token);

	Optional<User> findByFacebookIdOrEmail(String facebookId, String email);

	List<User> findAllByFacebookId(LinkedList<String> idsList);
}
