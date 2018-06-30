package binar.box.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.common.base.Optional;

import binar.box.domain.Lock;
import binar.box.domain.User;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public interface LockRepository extends JpaRepository<Lock, Long> {

	List<Lock> findByUser(User user);

	void deleteByUserAndDeleteToken(User user, String token);

	Optional<Lock> findByUserAndId(User user, long id);

}
