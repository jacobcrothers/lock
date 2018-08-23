package binar.box.dto;

import java.math.BigDecimal;
import java.util.List;

import binar.box.domain.LockTypeTemplate;

/**
 * Created by Timis Nicu Alexandru on 06-Aug-18.
 */
public class LockTypeTemplateDTO {

	private Long id;

	private Integer fontSize;

	private String fontStyle;

	private String fontColor;

	private List<FileDTO> filesDTO;

	private String lockType;

	private BigDecimal price;

	public LockTypeTemplateDTO(LockTypeTemplate lockTypeTemplate) {
		this.id = lockTypeTemplate.getId();
		this.fontSize = lockTypeTemplate.getFontSize();
		this.fontStyle = lockTypeTemplate.getFontStyle();
		this.fontColor = lockTypeTemplate.getFontColor();
		this.lockType = lockTypeTemplate.getLockType().getType();
		this.setPrice(lockTypeTemplate.getPrice().getPrice());
	}

	public LockTypeTemplateDTO() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public List<FileDTO> getFilesDTO() {
		return filesDTO;
	}

	public void setFilesDTO(List<FileDTO> filesDTO) {
		this.filesDTO = filesDTO;
	}

	public String getLockType() {
		return lockType;
	}

	public void setLockType(String lockType) {
		this.lockType = lockType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
