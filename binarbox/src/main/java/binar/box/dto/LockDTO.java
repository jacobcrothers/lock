package binar.box.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data

// 3 step save
public class LockDTO {

	private Long id;

	private String message;

	private Integer fontSize;

	private String fontStyle;

	private String fontColor;

	private String lockColor;

	private boolean paid;

	private boolean glitteringLight;

	private Long lockSection;

	private Long lockTemplate;

	private boolean privateLock;

	private Double x;

	private Double y;
}
