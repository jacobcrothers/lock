package binar.box.converter;

import binar.box.domain.Point;
import binar.box.dto.PointDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PointConverter {

    public PointDTO toDTO(Point point) {
        return PointDTO.builder()
                .id(point.getId())
                .x(point.getX())
                .y(point.getY())
                .build();
    }

    public List<PointDTO> toDTOList(List<Point> points) {
        return points.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
