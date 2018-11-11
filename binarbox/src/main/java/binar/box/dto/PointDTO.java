package binar.box.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointDTO {
    private Long id;
    private Double x;
    private Double y;
}
