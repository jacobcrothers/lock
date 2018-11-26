package binar.box.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FontDTO {
    private Integer fontSize;
    private String fontStyle;
    private String fontColor;
    private Long id;
}
