package binar.box.converter;

import binar.box.domain.Lock;
import binar.box.domain.LockType;
import binar.box.dto.LockTypeDTOResponse;
import binar.box.dto.LockTypeTemplateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class LockTypeConverter {

    @Autowired
    private FileConverter fileConverter;

    @Autowired
    private LockTypeTemplateConverter lockTypeTemplateConverter;

    public LockTypeDTOResponse lockToLockTypeResponse(LockType lockType){
        return LockTypeDTOResponse.builder()
                .id(lockType.getId())
                .price(lockType.getPrice().getPrice())
                .type(lockType.getType())
                .lockTypeTemplate(Objects.isNull(lockType.getLockTypeTemplate()) ? null :
                        lockTypeTemplateConverter.toDTOList(lockType.getLockTypeTemplate()))
                .filesDTO(Objects.isNull((lockType.getFiles())) ? null :
                        fileConverter.toDTOList(lockType.getFiles()))
                .build();
    }

    public List<LockTypeDTOResponse> toDTOList(List<LockType> lockTypes){
        return lockTypes.stream().map(this::lockToLockTypeResponse).collect(Collectors.toList());
    }
}
