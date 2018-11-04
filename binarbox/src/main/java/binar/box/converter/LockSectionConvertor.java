package binar.box.converter;

import binar.box.domain.LockSection;
import binar.box.dto.LockSectionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class LockSectionConvertor {

    @Autowired
    private LockConvertor lockConvertor;

    public LockSectionDTO toDTO(LockSection lockSection){
        return LockSectionDTO.builder()
                .lockResponseDTO(Objects.isNull(lockSection.getLock()) ? null
                        : lockConvertor.toResponseDTO(lockSection.getLock()))
                .point(lockSection.getId())
                .section(lockSection.getSection())
                .build();
    }

    public List<LockSectionDTO> toDTOList(List<LockSection> lockSections){
        return lockSections.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
