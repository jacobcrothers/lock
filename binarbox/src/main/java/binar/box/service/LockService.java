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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    @Autowired
    private PanelService panelService;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);


    public LockTypeDtoResponse addLockType(LockTypeDTO lockTypeDTO) {
        LockType lockType = new LockType();
        lockType.setPrice(lockTypeDTO.getPrice());
        lockType.setType(lockTypeDTO.getType());
        lockTypeRepository.save(lockType);
        return new LockTypeDtoResponse(lockType);
    }

    public List<LockTypeDtoResponse> getLockTypes() {
        List<LockType> lockTypes = lockTypeRepository.findAll();
        return lockTypes.stream().map(this::toLockTypeDtoResponse).collect(Collectors.toList());
    }

    private LockTypeDtoResponse toLockTypeDtoResponse(LockType lockType) {
        var lockTypeDtoResponse = new LockTypeDtoResponse();
        lockTypeDtoResponse.setId(lockType.getId());
        lockTypeDtoResponse.setPrice(lockType.getPrice());
        lockTypeDtoResponse.setType(lockType.getType());
        lockTypeDtoResponse.setFileDtoList(lockType.getFiles().stream().map(this::toFileDto).collect(Collectors.toList()));
        return lockTypeDtoResponse;
    }

    private FileDTO toFileDto(File file) {
        return new FileDTO(file);
    }

    public List<LockSection> getLockSections() {
        return lockSectionRepository.findAll();
    }

    public void addUserLock(LockDTO lockDTO) {
        executorService.submit(() -> panelService.maintainPanels());
        var user = userService.getAuthenticatedUser();
        var lockSection = getLockSection(lockDTO.getLockSection());
        var lockType = getLockType(lockDTO.getLockType());
        var lock = new Lock();
        lock.setLongitude(lockDTO.getLongitude());
        lock.setLatitude(lockDTO.getLatitude());
        lock.setUser(user);
        lock.setLockType(lockType);
        lock.setLockSection(lockSection);
        lock.setMessage(lockDTO.getMessage());
        lock.setFontSize(lockDTO.getFontSize());
        lock.setFontStyle(lockDTO.getFontStyle());
        lock.setCreatedDate(new Date());
        lock.setLastModifiedDate(new Date());
        var panel = panelService.getPanel(lockDTO.getPanelId());
        lock.setPanel(panel);
        lockRepository.save(lock);
    }

    private LockType getLockType(Long lockTypeId) {
        return lockTypeRepository.findById(lockTypeId).orElseThrow(() -> new LockBridgesException(Constants.LOCK_TYPE_NOT_FOUND));
    }

    private LockSection getLockSection(Long lockSectionId) {
        return lockSectionRepository.findById(lockSectionId).orElseThrow(() -> new LockBridgesException(Constants.LOCK_SECTION_NOT_FOUND));
    }

    public List<LockResponseDTO> getLocks() {
        User user = userService.getAuthenticatedUser();
        List<Lock> lockList = lockRepository.findByUser(user);
        return lockList.stream().map(this::toLockResponseDto).collect(Collectors.toList());
    }

    LockResponseDTO toLockResponseDto(Lock lock) {
        return new LockResponseDTO(lock);
    }

    public void removeUserLock(long id) {
        lockRepository.deleteById(id);
    }
}
