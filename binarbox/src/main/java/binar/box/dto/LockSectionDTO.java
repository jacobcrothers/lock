package binar.box.dto;

import binar.box.domain.LockSection;

/**
 * Created by Timis Nicu Alexandru on 23-Aug-18.
 */
public class LockSectionDTO {

	private String section;

	private Long point;

	private LockResponseDTO lockResponseDTO;

	public LockSectionDTO() {

	}

	public LockSectionDTO(LockSection lockSection) {
		this.section = lockSection.getSection();
		this.point = lockSection.getId();
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public LockResponseDTO getLockResponseDTO() {
		return lockResponseDTO;
	}

	public void setLockResponseDTO(LockResponseDTO lockResponseDTO) {
		this.lockResponseDTO = lockResponseDTO;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

}
