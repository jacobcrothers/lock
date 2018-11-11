package binar.box.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LockDTO {

	private Long id;

	@NotNull
	private String message;

	private Integer fontSize;

	private String fontStyle;

	private String fontColor;

	private String lockColor;

	private boolean paid;

	private boolean glitteringLight;

	@NotNull
	private Long lockSection;

	@NotNull
	private Long lockTypeTemplate;

	@NotNull
	private boolean privateLock;

	@NotNull
	private Double x;

	@NotNull
	private Double y;
}
