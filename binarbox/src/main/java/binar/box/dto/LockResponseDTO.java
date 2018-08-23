package binar.box.dto;

import java.math.BigDecimal;

import binar.box.domain.Lock;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public class LockResponseDTO {

	private long id;

	private Double longitude;

	private Double latitude;

	private String message;

	private Integer fontSize;

	private String fontStyle;

	private String fontColor;

	private String lockColor;

	private String lockSection;

	private LockTypeDtoResponse lockTypeDtoResponse;

	private Long panelId;

	private Boolean privateLock;

	private Boolean glitteringLight;

	private BigDecimal price;

	public LockResponseDTO() {

	}

	public LockResponseDTO(Lock lock) {
		this.id = lock.getId();
		this.longitude = lock.getLongitude();
		this.latitude = lock.getLatitude();
		this.message = lock.getMessage();
		this.fontSize = lock.getFontSize();
		this.fontStyle = lock.getFontStyle();
		this.fontColor = lock.getFontColor();
		this.lockColor = lock.getLockColor();
		this.lockSection = lock.getLockSection() == null ? null : lock.getLockSection().getSection();
		this.panelId = lock.getPanel() == null ? null : lock.getPanel().getId();
		this.privateLock = lock.isPrivateLock();
		this.glitteringLight = lock.isGlitteringLight();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
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

	public String getLockSection() {
		return lockSection;
	}

	public void setLockSection(String lockSection) {
		this.lockSection = lockSection;
	}

	public LockTypeDtoResponse getLockTypeDtoResponse() {
		return lockTypeDtoResponse;
	}

	public void setLockTypeDtoResponse(LockTypeDtoResponse lockTypeDtoResponse) {
		this.lockTypeDtoResponse = lockTypeDtoResponse;
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

}
