package binar.box.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@ToString
@Entity
@Table(name="locks")
public class Lock extends BaseEntity {

	@Column(name = "message")
	private String message;

	@Column(name = "font_size")
	private Integer fontSize;

	@Column(name = "font_style")
	private String fontStyle;

	@Column(name = "font_color")
	private String fontColor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lockSection_id")
	private LockSection lockSection;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lockType_id")
	private LockType lockType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lockTypeTemplate_id")
	private LockTypeTemplate lockTypeTemplate;

	@ManyToOne(fetch = FetchType.LAZY)
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

	@Column
	private boolean glitteringLight;
}
