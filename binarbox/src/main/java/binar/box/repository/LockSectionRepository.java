package binar.box.repository;

import binar.box.domain.LockSection;

import java.util.List;

public interface LockSectionRepository extends BaseJpaRepository<LockSection, Long> {

	List<LockSection> findBySection(char letter);

	List<LockSection> findByPanelId(long panelId);
}
