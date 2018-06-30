package binar.box.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public class LockDTO {

	@NotEmpty
	@NotNull
	private String message;

	@NotNull
	private Integer fontSize;

	@NotEmpty
	@NotNull
	private String fontStyle;

	@NotNull
	private Long lockSection;

	@NotNull
	private Long lockType;

	@NotNull
	private Double longitude;

	@NotNull
	private Double latitude;

	@NotNull
	private Long panelId;

	public LockDTO() {
	}

	public long getPanelId() {
		return panelId;
	}

	public void setPanelId(long panelId) {
		this.panelId = panelId;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
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
}
