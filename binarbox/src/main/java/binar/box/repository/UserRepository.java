package binar.box.repository;

import binar.box.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);

    Optional<User> findByConfirmEmailToken(String token);

    Optional<User> findByFacebookIdOrEmail(String facebookId, String email);
}
