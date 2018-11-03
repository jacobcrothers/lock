package binar.box.service;

import binar.box.converter.LockConvertor;
import binar.box.converter.LockSectionConvertor;
import binar.box.converter.LockTypeConverter;
import binar.box.converter.LockTypeTemplateConverter;
import binar.box.domain.*;
import binar.box.dto.*;
import binar.box.repository.LockRepository;
import binar.box.repository.LockSectionRepository;
import binar.box.repository.LockTypeRepository;
import binar.box.repository.LockTypeTemplateRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

	@Autowired
	private LockTypeConverter lockTypeConverter;

	@Autowired
	private LockConvertor lockConvertor;

	@Autowired
	private LockTypeTemplateRepository lockTypeTemplateRepository;

	@Autowired
	private LockSectionConvertor lockSectionConvertor;

	public LockTypeDTOResponse addLockType(LockTypeDTO lockTypeDTO) {
		LockType lockType = new LockType();
		lockType.setType(lockTypeDTO.getType());
		return lockTypeConverter.lockToLockTypeResponse(lockTypeRepository.save(lockType));
	}

	public List<LockTypeDTOResponse> getLockTypes() {
		return lockTypeConverter.toDTOList(lockTypeRepository.findAll());
	}

	public List<LockSectionDTO> getLockSections() {
		return lockSectionConvertor.toDTOList(lockSectionRepository.findAll());
	}

	public LockResponseDTO addOrUpdateUserLock(LockDTO lockDTO) {
		Lock lock;
		if (lockDTO.getId() == null) {
			lock = new Lock();
			lock.setCreatedDate(new Date());
		} else {
			lock = getLockById(lockDTO.getId());
		}
		lock = addOrUpdateUserLock(lockDTO, lock, userService.getAuthenticatedUser());
		return lockConvertor.toResponseDTO(lockRepository.save(lock));
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
		LockTypeTemplate lockTypeTemplate = null;
		if (lockDTO.getLockTypeTemplate() != null) {
			lockTypeTemplate = getLockTypeTemplate(lockDTO.getLockTypeTemplate());
		}
		setLockFields(lockDTO, user, lockSection, lockType, lockTypeTemplate, lock, panel);
		return lock;
	}

	private LockTypeTemplate getLockTypeTemplate(Long lockTypeTemplate) {
		return lockTypeTemplateRepository.findById(lockTypeTemplate)
				.orElseThrow(() -> new LockBridgesException(Constants.LOCK_SECTION_NOT_FOUND));
	}

	private void setLockFields(LockDTO lockDTO, User user, LockSection lockSection, LockType lockType,
			LockTypeTemplate lockTypeTemplate, Lock lock, Panel panel) {
		lock.setUser(user);
		lock.setLockType(lockType);
		lock.setLockTypeTemplate(lockTypeTemplate);
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
		return lockConvertor.toDTOList(lockRepository.
						findByUserAndPaidFalse(userService.
								getAuthenticatedUser()));
	}

	public void removeUserLock(String token) {
		var user = userService.getAuthenticatedUser();
		lockRepository.deleteByUserAndDeleteToken(user, token);
	}

	public void claimToRemoveUserLock(long id) {
		var user = userService.getAuthenticatedUser();
		Optional<Lock> lock = lockRepository.findByUserAndId(user, id);
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