package binar.box.converter;

import binar.box.domain.File;
import binar.box.dto.FileDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileConverter {

    public FileDTO toDTO(File file){
        return FileDTO.builder()
                .id(file.getId())
                .name(file.getFileName())
                .pathToFile(file.getPathToFile())
                .urlToFile(file.getUrlToFile())
                .type(file.getType())
                .build();
    }

    public List<FileDTO> toDTOList(List<File> files){
        return files.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
