package binar.box.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import binar.box.domain.File;
import binar.box.domain.Lock;
import binar.box.domain.LockSection;
import binar.box.domain.LockType;
import binar.box.domain.Panel;
import binar.box.domain.User;
import binar.box.dto.FileDTO;
import binar.box.dto.LockDTO;
import binar.box.dto.LockResponseDTO;
import binar.box.dto.LockTypeDTO;
import binar.box.dto.LockTypeDtoResponse;
import binar.box.repository.LockRepository;
import binar.box.repository.LockSectionRepository;
import binar.box.repository.LockTypeRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
@Service
@Transactional
public class LockService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private LockTypeRepository lockTypeRepository;

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
		lockTypeDtoResponse
				.setFileDtoList(lockType.getFiles().stream().map(this::toFileDto).collect(Collectors.toList()));
		return lockTypeDtoResponse;
	}

	private FileDTO toFileDto(File file) {
		return new FileDTO(file);
	}

	public List<LockSection> getLockSections() {
		return lockSectionRepository.findAll();
	}

	public void addOrUpdateUserLock(LockDTO lockDTO) {
		executorService.submit(() -> panelService.maintainPanels());
		var user = userService.getAuthenticatedUser();
		Lock lock = null;
		if (lockDTO.getId() == null) {
			lock = new Lock();
			lock.setCreatedDate(new Date());
		} else {
			lock = getLockById(lockDTO.getId());
		}
		lock = addOrUpdateUserLock(lockDTO, lock, user);
		lockRepository.save(lock);
	}

	private Lock getLockById(Long lockId) {
		return lockRepository.findById(lockId).orElseThrow(() -> new LockBridgesException(Constants.LOCK_NOT_FOUND));
	}

	private Lock addOrUpdateUserLock(LockDTO lockDTO, Lock lock, User user) {
		LockSection lockSection = null;
		if (lockDTO.getLockSection() != null) {
			lockSection = getLockSection(lockDTO.getLockSection());
		}
		var lockType = getLockType(lockDTO.getLockType());
		Panel panel = null;
		if (lockDTO.getPanelId() != null) {
			panel = panelService.getPanel(lockDTO.getPanelId());
		}
		setLockFields(lockDTO, user, lockSection, lockType, lock, panel);
		return lock;
	}

	private void setLockFields(LockDTO lockDTO, User user, LockSection lockSection, LockType lockType, Lock lock,
			Panel panel) {
		if (lockDTO.getLongitude() != null) {
			lock.setLongitude(lockDTO.getLongitude());
		}
		if (lockDTO.getLatitude() != null) {
			lock.setLatitude(lockDTO.getLatitude());
		}
		lock.setUser(user);
		lock.setLockType(lockType);
		lock.setLockSection(lockSection);
		lock.setMessage(lockDTO.getMessage());
		if (lockDTO.getFontSize() != null) {
			lock.setFontSize(lockDTO.getFontSize());
		}
		lock.setFontStyle(lockDTO.getFontStyle());
		lock.setFontColor(lockDTO.getFontColor());
		lock.setLockColor(lockDTO.getLockColor());
		lock.setPrivateLock(lockDTO.getPrivateLock() == null ? false : lockDTO.getPrivateLock());
		lock.setLastModifiedDate(new Date());
		lock.setPanel(panel);
	}

	private LockType getLockType(Long lockTypeId) {
		return lockTypeRepository.findById(lockTypeId)
				.orElseThrow(() -> new LockBridgesException(Constants.LOCK_TYPE_NOT_FOUND));
	}

	private LockSection getLockSection(Long lockSectionId) {
		return lockSectionRepository.findById(lockSectionId)
				.orElseThrow(() -> new LockBridgesException(Constants.LOCK_SECTION_NOT_FOUND));
	}

	public List<LockResponseDTO> getLocks() {
		var user = userService.getAuthenticatedUser();
		var lockList = lockRepository.findByUser(user);
		return lockList.stream().map(this::toLockResponseDto).collect(Collectors.toList());
	}

	LockResponseDTO toLockResponseDto(Lock lock) {
		return new LockResponseDTO(lock);
	}

	public void removeUserLock(String token) {
		var user = userService.getAuthenticatedUser();
		lockRepository.deleteByUserAndDeleteToken(user, token);
	}

	public void claimToRemoveUserLock(long id) {
		var user = userService.getAuthenticatedUser();
		var lock = lockRepository.findByUserAndId(user, id);
		if (!lock.isPresent()) {
			throw new LockBridgesException(Constants.LOCK_NOT_FOUND);
		}
		var lockEntity = lock.get();
		var token = UUID.randomUUID().toString();
		lockEntity.setDeleteToken(token);
		lockRepository.save(lockEntity);
		emailService.sendEmail(user.getEmail(), "Claim to remove user lock ",
				"Remove your lock using token : " + token);

	}

}