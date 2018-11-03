package binar.box.converter;

import binar.box.domain.Lock;
import binar.box.dto.LockResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class LockConvertor {

    @Autowired
    private LockSectionConvertor lockSectionConvertor;

    @Autowired
    private LockTypeConverter lockTypeConverter;

    public LockResponseDTO toResponseDTO(Lock lock){
        return LockResponseDTO.builder()
                .fontColor(lock.getFontColor())
                .fontSize(lock.getFontSize())
                .fontStyle(lock.getFontStyle())
                .id(lock.getId())
                .message(lock.getMessage())
                .privateLock(lock.isPrivateLock())
                .glitteringLight(lock.isGlitteringLight())
                .paid(lock.isPaid())
                .price(lock.getLockType().getPrice().getPrice().add(lock.getLockTypeTemplate().getPrice().getPrice()))
                .panelId(Objects.isNull(lock.getPanel())? null :lock.getPanel().getId())
                .lockSection(Objects.isNull(lock.getLockSection()) ? null : lockSectionConvertor.toDTO(lock.getLockSection()))
                .lockTypeDTOResponse(lockTypeConverter.lockToLockTypeResponse(lock.getLockType()))
                .build();
    }

    public List<LockResponseDTO> toDTOList(List<Lock> locks){
        return locks.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}
