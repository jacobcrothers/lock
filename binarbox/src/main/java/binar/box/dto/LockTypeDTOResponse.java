package binar.box.dto;

import java.math.BigDecimal;
import java.util.List;

import binar.box.domain.LockType;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public class LockTypeDTOResponse {

	private long id;

	private String type;

	private BigDecimal price;

	private List<FileDTO> filesDTO;

	private List<LockTypeTemplateDTO> lockTypeTemplate;

	public LockTypeDTOResponse() {
	}

	public LockTypeDTOResponse(LockType lockType) {
		this.id = lockType.getId();
		this.type = lockType.getType();
		this.setPrice(lockType.getPrice().getPrice());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<FileDTO> getFilesDTO() {
		return filesDTO;
	}

	public void setFilesDTO(List<FileDTO> filesDTO) {
		this.filesDTO = filesDTO;
	}

	public List<LockTypeTemplateDTO> getLockTypeTemplate() {
		return lockTypeTemplate;
	}

	public void setLockTypeTemplate(List<LockTypeTemplateDTO> lockTypeTemplate) {
		this.lockTypeTemplate = lockTypeTemplate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}