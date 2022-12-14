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

    private final LockConvertor lockConvertor;

    @Autowired
    public LockSectionConvertor(LockConvertor lockConvertor) {
        this.lockConvertor = lockConvertor;
    }

    public LockSectionDTO toDTO(LockSection lockSection){
        return LockSectionDTO.builder()
                .lockResponseDTOs(Objects.isNull(lockSection) ? null
                        : lockConvertor.toDTOList(lockSection.getLocks()))
                .section(lockSection.getSection())
                .build();
    }

    public List<LockSectionDTO> toDTOList(List<LockSection> lockSections){
        return lockSections.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
