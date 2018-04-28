package binar.box.service;

import binar.box.domain.*;
import binar.box.dto.*;
import binar.box.repository.FileRepository;
import binar.box.repository.LockRepository;
import binar.box.repository.LockSectionRepository;
import binar.box.repository.LockTypeRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
@Service
@Transactional
public class LockService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private LockTypeRepository lockTypeRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private LockSectionRepository lockSectionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LockRepository lockRepository;


    public LockTypeDtoResponse addLockType(LockTypeDto lockTypeDto) {
        LockType lockType = new LockType();
        lockType.setPrice(lockTypeDto.getPrice());
        lockType.setType(lockTypeDto.getType());
        lockTypeRepository.save(lockType);
        return new LockTypeDtoResponse(lockType);
    }

    public List<LockTypeDtoResponse> getLockTypes() {
        List<LockType> lockTypes = lockTypeRepository.findAll();
        return lockTypes.stream().map(this::toLockTypeDtoResponse).collect(Collectors.toList());
    }

    private LockTypeDtoResponse toLockTypeDtoResponse(LockType lockType) {
        LockTypeDtoResponse lockTypeDtoResponse = new LockTypeDtoResponse();
        lockTypeDtoResponse.setId(lockType.getId());
        lockTypeDtoResponse.setPrice(lockType.getPrice());
        lockTypeDtoResponse.setType(lockType.getType());
        lockTypeDtoResponse.setFileDtoList(lockType.getFiles().stream().map(this::toFileDto).collect(Collectors.toList()));
        return lockTypeDtoResponse;
    }

    private FileDto toFileDto(File file) {
        return new FileDto(file);
    }

    public List<LockSection> getLockSections() {
        return lockSectionRepository.findAll();
    }

    public void addUserLock(LockDto lockDto) {
        Random randomLongitude = new Random();
        Random randomLatitude = new Random();
        User user = userService.getAuthenticatedUser();
        LockSection lockSection = getLockSection(lockDto.getLockSection());
        LockType lockType = getLockType(lockDto.getLockType());
        Lock lock = new Lock();
        lock.setLongitude(randomLongitude.nextDouble());
        lock.setLatitude(randomLatitude.nextDouble());
        lock.setUser(user);
        lock.setLockType(lockType);
        lock.setLockSection(lockSection);
        lock.setMessage(lockDto.getMessage());
        lock.setFontSize(lockDto.getFontSize());
        lock.setFontStyle(lockDto.getFontStyle());
        lock.setCreatedDate(new Date());
        lock.setLastModifiedDate(new Date());
        lockRepository.save(lock);
    }

    private LockType getLockType(Long lockTypeId) {
        return lockTypeRepository.findById(lockTypeId).orElseThrow(() -> new LockBridgesException(Constants.LOCK_TYPE_NOT_FOUND));
    }

    private LockSection getLockSection(Long lockSectionId) {
        return lockSectionRepository.findById(lockSectionId).orElseThrow(() -> new LockBridgesException(Constants.LOCK_SECTION_NOT_FOUND));
    }

    public List<LockResponseDto> getLocks() {
        User user = userService.getAuthenticatedUser();
        List<Lock> lockList = lockRepository.findByUser(user);
        return lockList.stream().map(this::toLockResponseDto).collect(Collectors.toList());
    }

    private LockResponseDto toLockResponseDto(Lock lock) {
        return new LockResponseDto(lock);
    }
}
