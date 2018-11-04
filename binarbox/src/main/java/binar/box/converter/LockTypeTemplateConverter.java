package binar.box.converter;

import binar.box.domain.LockTypeTemplate;
import binar.box.dto.LockTypeTemplateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class LockTypeTemplateConverter {

    @Autowired
    private FileConverter fileConverter;

    public LockTypeTemplateDTO toDTO(LockTypeTemplate lockTypeTemplate){
        return LockTypeTemplateDTO.builder()
                .filesDTO(Objects.isNull(lockTypeTemplate.getFiles()) ? null : fileConverter.toDTOList(lockTypeTemplate.getFiles()))
                .fontColor(lockTypeTemplate.getFontColor())
                .fontSize(lockTypeTemplate.getFontSize())
                .fontStyle(lockTypeTemplate.getFontStyle())
                .id(lockTypeTemplate.getId())
                .lockType(lockTypeTemplate.getLockType().getType())
                .price(lockTypeTemplate.getPrice().getPrice())
                .build();
    }

    public List<LockTypeTemplateDTO> toDTOList(List<LockTypeTemplate> lockTypeTemplates){
        return lockTypeTemplates.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
