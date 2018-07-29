package binar.box.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import binar.box.domain.Panel;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
@Repository
public interface PanelRepository extends JpaRepository<Panel, Long> {

	@Query(value = "SELECT * FROM panel_entity WHERE panel_entity.location=:location ORDER BY RAND() LIMIT 8;", nativeQuery = true)
	List<Panel> findAllPanelsBasedOnLocation(@Param("location") String string);

	@Query(value = "SELECT COUNT(*) FROM (\n" + "SELECT panel_entity.id, COUNT(lock_entity.id) AS lock_on_panel\n"
			+ "FROM\n" + "    panel_entity LEFT JOIN\n" + "    lock_entity ON panel_entity.id = lock_entity.panel_id\n"
			+ "GROUP BY panel_entity.id ORDER BY lock_on_panel DESC ) AS a  WHERE a.lock_on_panel <= 29", nativeQuery = true)
	Optional<BigInteger> countPanels();

	@Query(value = "CALL insertRandomPanelsEntity(:number)", nativeQuery = true)
	List<Object[]> addPanels(@Param("number") int number);

	@Query(value = "SELECT DISTINCT  lb.panel_entity.id,lb.panel_entity.created_date,lb.panel_entity.last_modified_date FROM lb.panel_entity INNER JOIN \r\n"
			+ "lb.lock_entity ON lb.lock_entity.panel_id = lb.panel_entity.id WHERE lb.lock_entity.user_id=:userId AND lb.lock_entity.paid=1", nativeQuery = true)
	List<Panel> findByUser(@Param("userId") long userId);
}
