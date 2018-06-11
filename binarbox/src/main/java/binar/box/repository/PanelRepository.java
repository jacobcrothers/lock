package binar.box.repository;

import binar.box.domain.Panel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
@Repository
public interface PanelRepository extends JpaRepository<Panel, Long> {

    @Query(value = "SELECT * FROM panel_entity ORDER BY RAND() LIMIT 30;", nativeQuery = true)
    List<Panel> findRandomPanels();
}
