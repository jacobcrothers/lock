package binar.box.dto;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public class LockDTO {

	private Long id;

	private String message;

	private Integer fontSize;

	private String fontStyle;

	private String fontColor;

	private String lockColor;

	private Long lockSection;

	private Long lockType;

	private Double longitude;

	private Double latitude;

	private Long panelId;

	private Boolean privateLock;

	public LockDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Long getLockSection() {
		return lockSection;
	}

	public void setLockSection(Long lockSection) {
		this.lockSection = lockSection;
	}

	public Long getLockType() {
		return lockType;
	}

	public void setLockType(Long lockType) {
		this.lockType = lockType;
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

}
