package binar.box.repository;

import binar.box.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseJpaRepository<User, String> {

	Optional<User> findByEmail(String email);

}
