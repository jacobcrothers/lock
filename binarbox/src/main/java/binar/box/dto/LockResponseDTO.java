package binar.box.dto;

import java.math.BigDecimal;

import binar.box.domain.Lock;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public class LockResponseDTO {

	private long id;

	private String message;

	private Integer fontSize;

	private String fontStyle;

	private String fontColor;

	private String lockColor;

	private LockSectionDTO lockSection;

	private LockTypeDTOResponse lockTypeDTOResponse;

	private Long panelId;

	private Boolean privateLock;

	private Boolean glitteringLight;

	private BigDecimal price;

	private boolean paid;

	public LockResponseDTO() {

	}

	public LockResponseDTO(Lock lock) {
		this.id = lock.getId();
		this.message = lock.getMessage();
		this.fontSize = lock.getFontSize();
		this.fontStyle = lock.getFontStyle();
		this.fontColor = lock.getFontColor();
		this.lockColor = lock.getLockColor();
		this.panelId = lock.getPanel() == null ? null : lock.getPanel().getId();
		this.lockSection = lock.getLockSection() == null ? null : new LockSectionDTO(lock.getLockSection());
		this.privateLock = lock.isPrivateLock();
		this.glitteringLight = lock.isGlitteringLight();
		this.paid = lock.isPaid();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public String getLockColor() {
		return lockColor;
	}

	public void setLockColor(String lockColor) {
		this.lockColor = lockColor;
	}

	public LockTypeDTOResponse getLockTypeDTOResponse() {
		return lockTypeDTOResponse;
	}

	public void setLockTypeDTOResponse(LockTypeDTOResponse lockTypeDTOResponse) {
		this.lockTypeDTOResponse = lockTypeDTOResponse;
	}

	public Long getPanelId() {
		return panelId;
	}

	public void setPanelId(Long panelId) {
		this.panelId = panelId;
	}

	public Boolean getPrivateLock() {
		return privateLock;
	}

	public void setPrivateLock(Boolean privateLock) {
		this.privateLock = privateLock;
	}

	public Boolean getGlitteringLight() {
		return glitteringLight;
	}

	public void setGlitteringLight(Boolean glitteringLight) {
		this.glitteringLight = glitteringLight;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public LockSectionDTO getLockSection() {
		return lockSection;
	}

	public void setLockSection(LockSectionDTO lockSection) {
		this.lockSection = lockSection;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

}
