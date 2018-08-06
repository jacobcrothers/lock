package binar.box.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by Timis Nicu Alexandru on 06-Aug-18.
 */
@Entity
@Table(name = "lock_type_template")
public class LockTypeTemplate extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "font_size")
	private Integer fontSize;

	@Column(name = "font_style")
	private String fontStyle;

	@Column(name = "font_color")
	private String fontColor;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lockTypeTemplate")
	private List<File> files;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lock_type")
	private LockType lockType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lock_template_price")
	private LockTemplatePrice templatePriceRange;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lock_type_price")
	private LockTypePrice price;

	public LockTypeTemplate() {
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

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public LockType getLockType() {
		return lockType;
	}

	public void setLockType(LockType lockType) {
		this.lockType = lockType;
	}

	public LockTemplatePrice getTemplatePriceRange() {
		return templatePriceRange;
	}

	public void setTemplatePriceRange(LockTemplatePrice templatePriceRange) {
		this.templatePriceRange = templatePriceRange;
	}

	public LockTypePrice getPrice() {
		return price;
	}

	public void setPrice(LockTypePrice price) {
		this.price = price;
	}

}
