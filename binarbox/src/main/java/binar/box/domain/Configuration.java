package binar.box.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuration_entity")
public class Configuration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "panel_max_size_of_locks")
	private int panelMaxSizeOfLocks;

	@Column(name = "random_locks_on_user_panel")
	private int randomLocksOnUserPanel;

	@Column(name = "glittering_lights_on_locks")
	private int glitteringLightsOnLocks;

	@Column(name = "max_panels_view")
	private int maxPanelsView;

	public Configuration() {

	}

	public long getId() {
		return id;
	}

	public int getPanelMaxSizeOfLocks() {
		return panelMaxSizeOfLocks;
	}

	public void setPanelMaxSizeOfLocks(int panelMaxSizeOfLocks) {
		this.panelMaxSizeOfLocks = panelMaxSizeOfLocks;
	}

	public int getRandomLocksOnUserPanel() {
		return randomLocksOnUserPanel;
	}

	public void setRandomLocksOnUserPanel(int randomLocksOnUserPanel) {
		this.randomLocksOnUserPanel = randomLocksOnUserPanel;
	}

	public int getGlitteringLightsOnLocks() {
		return glitteringLightsOnLocks;
	}

	public void setGlitteringLightsOnLocks(int glitteringLightsOnLocks) {
		this.glitteringLightsOnLocks = glitteringLightsOnLocks;
	}

	public int getMaxPanelsView() {
		return maxPanelsView;
	}

	public void setMaxPanelsView(int maxPanelsView) {
		this.maxPanelsView = maxPanelsView;
	}

	public void setId(long id) {
		this.id = id;
	}

}
