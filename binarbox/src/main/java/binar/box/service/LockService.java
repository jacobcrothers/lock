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
	private LockTypeConverter lockTypeConverter;

	@Autowired
	private LockConvertor lockConvertor;

	@Autowired
	private LockTypeTemplateRepository lockTypeTemplateRepository;

	@Autowired
	private LockSectionConvertor lockSectionConvertor;

	@Autowired
	private PriceRepository priceRepository;

	@Autowired
	private PointRepository pointRepository;

	public LockCategoryDTOResponse addLockCategory(LockCategoryDTO lockCategoryDTO) {
		LockCategory lockCategory = new LockCategory();
		lockCategory.setCategory(lockCategoryDTO.getCategory());

		Price price = new Price();
		price.setPrice(lockCategoryDTO.getPrice());
		lockCategory.setPrice(priceRepository.save(price));

		return lockTypeConverter.lockToLockTypeResponse(lockTypeRepository.save(lockCategory));
	}

	public List<LockCategoryDTOResponse> getLockCategories() {
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
		Lock lock = lockRepository.findOne(lockDTO.getId());
		Lock updatedLock = populateEntity(lockDTO, lock);
		updatedLock.setId(lockDTO.getId());

		return lockConvertor.toResponseDTO(lockRepository.save(updatedLock));
	}

	private Lock populateEntity(LockDTO lockDTO, Lock lock) {
		Point point = new Point();
		if (Objects.isNull(lockDTO.getId())){
			point.setX(lockDTO.getX());
			point.setY(lockDTO.getY());
			pointRepository.save(point);
		} else {
			point = lock.getPoint();
			point.setX(lockDTO.getX());
			point.setY(lockDTO.getY());
			pointRepository.save(point);
		}

		LockSection lockSection=Objects.isNull(lockDTO.getLockSection()) ? null :
				lockSectionRepository.findOne(lockDTO.getLockSection());

		LockTypeTemplate lockTypeTemplate=Objects.isNull(lockDTO.getLockTypeTemplate()) ? null :
				lockTypeTemplateRepository.findOne(lockDTO.getLockTypeTemplate());

		return lockConvertor.toEntity(lockDTO,
				                      lock,
				                      lockSection,
				                      lockTypeTemplate,
				                      point,
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