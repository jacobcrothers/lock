package binar.box.converter;

import binar.box.domain.File;
import binar.box.domain.Font;
import binar.box.dto.FileDTO;
import binar.box.dto.FontDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FontConverter {

    public FontDTO toDTO(Font font){
        return FontDTO.builder()
                .id(font.getId())
                .fontColor(font.getFontColor())
                .fontSize(font.getFontSize())
                .fontStyle(font.getFontStyle())
                .build();
    }

    public List<FontDTO> toDTOList(List<Font> files){
        return files.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
