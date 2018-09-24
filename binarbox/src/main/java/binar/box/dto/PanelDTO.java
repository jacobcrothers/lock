package binar.box.dto;

import java.util.List;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
public class PanelDTO {

	private long id;

	private List<LockSectionDTO> lockSectionDTO;

	public PanelDTO() {
	}

	public List<LockSectionDTO> getLockSectionDTO() {
		return lockSectionDTO;
	}

	public void setLockSectionDTO(List<LockSectionDTO> lockSectionDTO) {
		this.lockSectionDTO = lockSectionDTO;
	}

}
