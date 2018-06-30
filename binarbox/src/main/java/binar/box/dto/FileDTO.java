package binar.box.dto;

import binar.box.domain.File;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public class FileDTO {

	private long id;

	private String name;

	public FileDTO() {
	}

	public FileDTO(File file) {
		this.id = file.getId();
		this.name = file.getFileName();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
