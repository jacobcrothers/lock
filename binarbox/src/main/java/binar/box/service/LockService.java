package binar.box.service;

import binar.box.converter.LockConvertor;
import binar.box.converter.LockSectionConvertor;
import binar.box.converter.LockTypeConverter;
import binar.box.domain.*;
import binar.box.dto.*;
import binar.box.repository.*;
import binar.box.util.Constants;
import binar.box.util.Exceptions.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

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
	private PanelRepository panelRepository;

	@Autowired
	private LockTypeConverter lockTypeConverter;

	@Autowired
	private LockConvertor lockConvertor;

	@Autowired
	private LockTypeTemplateRepository lockTypeTemplateRepository;

	@Autowired
	private LockSectionConvertor lockSectionConvertor;

	@Autowired
	private PriceRepository priceRepository;

	public LockTypeDTOResponse addLockType(LockTypeDTO lockTypeDTO) {
		LockType lockType = new LockType();
		lockType.setType(lockTypeDTO.getType());

		Price price = new Price();
		price.setPrice(lockTypeDTO.getPrice());
		lockType.setPrice(priceRepository.save(price));

		return lockTypeConverter.lockToLockTypeResponse(lockTypeRepository.save(lockType));
	}

	public List<LockTypeDTOResponse> getLockTypes() {
		return lockTypeConverter.toDTOList(lockTypeRepository.findAll());
	}

	public List<LockSectionDTO> getLockSections() {
		return lockSectionConvertor.toDTOList(lockSectionRepository.findAll());
	}

    public LockResponseDTO createUserLock(LockDTO lockDTO){
		Lock lock = populateEntity(lockDTO, new Lock());

        return lockConvertor.toResponseDTO(lockRepository.save(lock));
    }

	public LockResponseDTO updateUserLock(LockDTO lockDTO){
		if (Objects.isNull(lockDTO.getId()))
			throw new LockBridgesException(Constants.LOCK_NOT_FOUND);
		Lock lock = lockRepository.findById(lockDTO.getId())
				.orElseThrow(() -> new LockBridgesException(Constants.LOCK_NOT_FOUND));
		Lock updatedLock = populateEntity(lockDTO, lock);
		updatedLock.setId(lockDTO.getId());

		return lockConvertor.toResponseDTO(lockRepository.save(updatedLock));
	}

	private Lock populateEntity(LockDTO lockDTO, Lock lock) {
		LockSection lockSection=Objects.isNull(lockDTO.getLockSection()) ? null :
				lockSectionRepository.findById(lockDTO.getLockSection())
						.orElseThrow(() -> new LockBridgesException(Constants.LOCK_SECTION_NOT_FOUND));

		Panel panel= Objects.isNull(lockDTO.getPanelId()) ? null :
				panelRepository.findById(lockDTO.getPanelId())
						.orElseThrow(() -> new LockBridgesException(Constants.PANEL_NOT_FOUND));

		LockType lockType=Objects.isNull(lockDTO.getLockType()) ? null :
				lockTypeRepository.findByIdWithTemplatePriceAndFile(lockDTO.getLockType())
						.orElseThrow(() -> new LockBridgesException(Constants.LOCK_TYPE_NOT_FOUND));

		LockTypeTemplate lockTypeTemplate=Objects.isNull(lockDTO.getLockType()) ? null :
				lockTypeTemplateRepository.findById(lockDTO.getLockTypeTemplate())
						.orElseThrow(() -> new LockBridgesException(Constants.LOCK_TYPE_TEMPLATE_NOT_FOUND));

		return lockConvertor.toEntity(lockDTO,
				                      lock,
				                      lockSection,
				                      panel,
				                      lockType,
				                      lockTypeTemplate,
				                      userService.getAuthenticatedUser());
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