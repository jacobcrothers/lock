package binar.box.dto;

import binar.box.domain.LockSection;

/**
 * Created by Timis Nicu Alexandru on 23-Aug-18.
 */
public class LockSectionDTO {

	private Long id;

	private String section;

	private LockResponseDTO lockResponseDTO;

	public LockSectionDTO() {

	}

	public LockSectionDTO(LockSection lockSection) {
		this.id = lockSection.getId();
		this.section = lockSection.getSection();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

}
