package binar.box.converter;

import binar.box.domain.LockTemplate;
import binar.box.dto.LockTemplateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LockTemplateConverter {

    @Autowired
    private FileConverter fileConverter;

    public LockTemplateDTO toDTO(LockTemplate lockTemplate){
        return LockTemplateDTO.builder()
                .filesDTO(fileConverter.toDTOList(lockTemplate.getFiles()))
                .fontColor(lockTemplate.getFontColor())
                .fontSize(lockTemplate.getFontSize())
                .fontStyle(lockTemplate.getFontStyle())
                .id(lockTemplate.getId())
                .lockCategory(lockTemplate.getLockCategory().getCategory())
                .price(lockTemplate.getPrice().getPrice())
                .build();
    }

    public List<LockTemplateDTO> toDTOList(List<LockTemplate> lockTemplates){
        return lockTemplates.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
