package binar.box.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.google.common.base.Optional;

import binar.box.domain.Lock;
import binar.box.domain.User;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public interface LockRepository extends JpaRepository<Lock, Long> {

	List<Lock> findByUserAndPaidFalse(User user);

	void deleteByUserAndDeleteToken(User user, String token);

	Optional<Lock> findByUserAndId(User user, long id);

	List<Lock> findByUserInAndPrivateLockFalse(List<User> applicationFriends);

	@Query(value = "SELECT l.id,l.longitude,l.latitude,l.message,l.font_size,l.font_style,l.font_color,l.paid,l.private_lock,l.lock_color,l.lock_section_id,l.lock_type_id,l.user_id,l.delete_token,l.created_date,l.last_modified_date,l.panel_id FROM lock_entity AS l \r\n"
			+ "INNER JOIN user AS u ON u.id = l.user_id\r\n"
			+ "WHERE l.panel_id = :panelId AND l.private_lock=0 OR (l.user_id = :userId AND l.private_lock=1) OR (u.facebook_id NOT IN(:facebookFriendsIds) AND l.private_lock=1)", nativeQuery = true)
	List<Lock> findUserPanelLocksAndHidePrivateFriendsLocks(@Param("userId") String userId,
			@Param("panelId") long panelId, @Param("facebookFriendsIds") List<String> facebookFriendsIds);

	List<Lock> findByPanelIdAndPaidTrue(Long id);

	List<Lock> findAllByUserIdAndPrivateLockFalse(List<String> facebookUserFriends);

	List<Lock> findByUser(User user);

}
