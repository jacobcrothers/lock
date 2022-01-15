package binar.box.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
        
        @Column(name = "user_id")
	private String userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lockSection_id")
	private LockSection lockSection;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id")
//	private User userId;

	@OneToMany
	@JoinTable(name="LockFile",
			joinColumns = @JoinColumn( name="lock_id"),
			inverseJoinColumns = @JoinColumn( name="file_id"))
	private List<File> files = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lockTemplate_id")
	private LockTemplate lockTemplate;

	@Column(name = "delete_token")
	private String deleteToken;

	@Column(name = "paid")
	private boolean paid;

	@Column(name = "lock_color")
	private String lockColor;

	@Column(name = "private_lock")
	private boolean privateLock;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "point_id")
	private Point point;
}
