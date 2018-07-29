package binar.box.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by Timis Nicu Alexandru on 16-Apr-18.
 */
@Entity
@Table(name = "lock_entity")
public class Lock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "message")
	private String message;

	@Column(name = "font_size")
	private Integer fontSize;

	@Column(name = "font_style")
	private String fontStyle;

	@Column(name = "font_color")
	private String fontColor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lock_section_id")
	private LockSection lockSection;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lock_type_id")
	private LockType lockType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "panel_id")
	private Panel panel;

	@Column(name = "delete_token")
	private String deleteToken;

	@Column(name = "paid")
	private boolean paid;

	@Column(name = "lock_color")
	private String lockColor;

	@Column(name = "private_lock")
	private boolean privateLock;

	@Transient
	private boolean glitteringLight;

	public Lock() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public LockSection getLockSection() {
		return lockSection;
	}

	public void setLockSection(LockSection lockSection) {
		this.lockSection = lockSection;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LockType getLockType() {
		return lockType;
	}

	public void setLockType(LockType lockType) {
		this.lockType = lockType;
	}

	public Panel getPanel() {
		return panel;
	}

	public void setPanel(Panel panel) {
		this.panel = panel;
	}

	public String getDeleteToken() {
		return deleteToken;
	}

	public void setDeleteToken(String deleteToken) {
		this.deleteToken = deleteToken;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public String getLockColor() {
		return lockColor;
	}

	public void setLockColor(String lockColor) {
		this.lockColor = lockColor;
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
