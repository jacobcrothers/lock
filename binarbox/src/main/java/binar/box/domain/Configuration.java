package binar.box.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@ToString
@Entity
@Table(name = "configuration_entity")
public class Configuration extends BaseEntity{

	@Column(name = "panel_max_size_of_locks")
	private int panelMaxSizeOfLocks;

	@Column(name = "random_locks_on_user_panel")
	private int randomLocksOnUserPanel;

	@Column(name = "glittering_lights_on_locks")
	private int glitteringLightsOnLocks;

	@Column(name = "max_panels_view")
	private int maxPanelsView;
}
