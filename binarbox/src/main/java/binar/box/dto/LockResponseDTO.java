package binar.box.dto;

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

	private String lockType;

	private Long panelId;

	private boolean privateLock;

	private boolean glitteringLight;

	public LockResponseDTO(Lock lock) {
		this.id = lock.getId();
		this.longitude = lock.getLongitude();
		this.latitude = lock.getLatitude();
		this.message = lock.getMessage();
		this.fontSize = lock.getFontSize();
		this.fontStyle = lock.getFontStyle();
		this.fontColor = lock.getFontColor();
		this.fontColor = lock.getLockColor();
		this.lockSection = lock.getLockSection() == null ? null : lock.getLockSection().getSection();
		this.lockType = lock.getLockType() == null ? null : lock.getLockType().getType();
		this.panelId = lock.getPanel() == null ? null : lock.getPanel().getId();
		this.privateLock = lock.isPrivateLock();
		this.glitteringLight = lock.isGlitteringLight();

	}

	public LockResponseDTO() {
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

	public String getLockType() {
		return lockType;
	}

	public void setLockType(String lockType) {
		this.lockType = lockType;
	}

	public Long getPanelId() {
		return panelId;
	}

	public void setPanelId(Long panelId) {
		this.panelId = panelId;
	}

	public boolean isPrivateLock() {
		return privateLock;
	}

	public void setPrivateLock(boolean privateLock) {
		this.privateLock = privateLock;
	}

	public boolean isGlitteringLight() {
		return glitteringLight;
	}

	public void setGlitteringLight(boolean glitteringLight) {
		this.glitteringLight = glitteringLight;
	}

}
