package binar.box.converter;

import binar.box.domain.*;
import binar.box.dto.LockDTO;
import binar.box.dto.LockResponseDTO;
import binar.box.dto.LockStepOneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class LockConvertor {

    @Autowired
    private LockCategoryConverter lockCategoryConverter;

    @Autowired
    private FileConverter fileConverter;

    @Autowired
    private PointConverter pointConverter;

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
                .price(lock.getLockTemplate().getLockCategory().getPrice().getPrice().add(lock.getLockTemplate().getPrice().getPrice()))
                .panelId(Objects.isNull(lock.getLockSection())? null :lock.getLockSection().getPanel().getId())
                .lockSectionId(Objects.isNull(lock.getLockSection()) ? null : lock.getLockSection().getId())
                .pointDTO(Objects.isNull(lock.getPoint()) ? null : pointConverter.toDTO(lock.getPoint()))
                .lockCategoryDTOResponse(Objects.isNull(lock.getLockTemplate()) ? null : lockCategoryConverter.lockToLockCategoryResponse(lock.getLockTemplate().getLockCategory()))
                .fileDTO(Objects.isNull(lock.getFile()) ? null : fileConverter.toDTO(lock.getFile()))
                .build();
    }

    public List<LockResponseDTO> toDTOList(List<Lock> locks){
        return locks.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public Lock toEntity(LockDTO lockDTO, Lock lock, LockSection lockSection, LockTemplate lockTemplate, Point point, User user){
        lock.setFontColor(Objects.isNull(lockDTO.getFontColor()) ? lock.getFontColor() : lockDTO.getFontColor());
        lock.setFontSize(Objects.isNull(lockDTO.getFontSize()) ? lock.getFontSize() : lockDTO.getFontSize());
        lock.setFontStyle(Objects.isNull(lockDTO.getFontStyle()) ? lock.getFontStyle() : lockDTO.getFontStyle());
        lock.setGlitteringLight(Objects.isNull(lockDTO.isGlitteringLight()) ? lock.isGlitteringLight() : lockDTO.isGlitteringLight());
        lock.setLockColor(Objects.isNull(lockDTO.getLockColor()) ? lock.getLockColor() : lockDTO.getLockColor());
        lock.setMessage(Objects.isNull(lockDTO.getMessage()) ? lock.getMessage() : lockDTO.getMessage());
        lock.setPrivateLock(Objects.isNull(lockDTO.isPrivateLock()) ? lock.isPrivateLock() : lockDTO.isPrivateLock());
        lock.setPaid(Objects.isNull(lockDTO.isPaid()) ? lock.isPaid() : lockDTO.isPaid());
        lock.setLockSection(Objects.isNull(lockSection) ? lock.getLockSection() : lockSection);
        lock.setLockTemplate(Objects.isNull(lockTemplate) ? lock.getLockTemplate() : lockTemplate);
        lock.setPoint(Objects.isNull(point) ? lock.getPoint() : point);
        lock.setUser(user);

        return lock;
    }

    public LockStepOneDTO toStepOneDTO(Lock lock) {
        LockStepOneDTO lockStepOneDTO = new LockStepOneDTO();
        lockStepOneDTO.setId(lock.getId());
        lockStepOneDTO.setLockTemplate(lock.getLockTemplate().getId());
        lockStepOneDTO.setMessage(lock.getMessage());
        lockStepOneDTO.setPrivateLock(lock.isPrivateLock());

        return lockStepOneDTO;
    }
}
