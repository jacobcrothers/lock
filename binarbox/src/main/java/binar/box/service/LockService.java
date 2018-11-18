package binar.box.service;

import binar.box.converter.LockConvertor;
import binar.box.converter.LockSectionConvertor;
import binar.box.converter.LockCategoryConverter;
import binar.box.domain.*;
import binar.box.dto.*;
import binar.box.repository.*;
import binar.box.util.Constants;
import binar.box.util.Exceptions.LockBridgesException;
import binar.box.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class LockService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private LockCategoryRepository lockCategoryRepository;

	@Autowired
	private LockSectionRepository lockSectionRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private LockRepository lockRepository;

	@Autowired
	private LockCategoryConverter lockCategoryConverter;

	@Autowired
	private LockConvertor lockConvertor;

	@Autowired
	private LockTemplateRepository lockTemplateRepository;

	@Autowired
	private LockSectionConvertor lockSectionConvertor;

	@Autowired
	private PriceRepository priceRepository;

	@Autowired
	private PointRepository pointRepository;

	@Autowired
	private FileService	fileService;

	public LockCategoryDTOResponse addLockCategory(LockCategoryDTO lockCategoryDTO) {
		LockCategory lockCategory = new LockCategory();
		lockCategory.setCategory(lockCategoryDTO.getCategory());

		Price price = new Price();
		price.setPrice(lockCategoryDTO.getPrice());
		lockCategory.setPrice(priceRepository.save(price));

		return lockCategoryConverter.lockToLockCategoryResponse(lockCategoryRepository.save(lockCategory));
	}

	public List<LockCategoryDTOResponse> getLockCategories() {
		return lockCategoryConverter.toDTOList(lockCategoryRepository.findAll());
	}

	public List<LockSectionDTO> getLockSections() {
		return lockSectionConvertor.toDTOList(lockSectionRepository.findAll());
	}

    public LockStepOneDTO createUserLock(LockStepOneDTO lockStepOneDTO) throws IOException {
		Lock lock = new Lock();

		lock.setLockTemplate(lockTemplateRepository.findOne(lockStepOneDTO.getLockTemplate()));
		lock.setMessage(lockStepOneDTO.getMessage());
		lock.setPrivateLock(lockStepOneDTO.getPrivateLock());

		lockRepository.save(lock);

		saveTextOnImage(lock);

        return lockConvertor.toStepOneDTO(lockRepository.save(lock));
    }

    @Deprecated
	public LockResponseDTO createUserLock(LockDTO lockDTO) throws IOException {
		Lock lock = populateEntity(lockDTO, new Lock());
		lockRepository.save(lock);
		saveTextOnImage(lock);
		return lockConvertor.toResponseDTO(lockRepository.save(lock));
	}

	private void saveTextOnImage(Lock lock) throws IOException {
		File lockFile =  lock.getLockTemplate().getFiles().stream()
				.filter(f -> f.getType().equals(File.Type.PARTIALY_ERASED_TEMPLATE_WITH_TEXT))
				.findAny()
				.orElseThrow(() -> new LockBridgesException("Lock partialy erased image not found"));

		binar.box.domain.File fileEntity = fileService.getFile(lockFile.getId());
		java.io.File diskFile = new java.io.File(fileEntity.getPathToFile());

		ImageUtils.addTextToImage(diskFile, ImageUtils.returnPathToImages(), lock.getMessage());
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
		LockSection lockSection=Objects.isNull(lockDTO.getLockSection()) ? null :
				lockSectionRepository.findOne(lockDTO.getLockSection());

		LockTemplate lockTemplate =Objects.isNull(lockDTO.getLockTemplate()) ? null :
				lockTemplateRepository.findOne(lockDTO.getLockTemplate());

		return lockConvertor.toEntity(lockDTO,
				                      lock,
				                      lockSection,
				                      lockTemplate,
									  addPoint(lockDTO, lock),
				                      userService.getAuthenticatedUser());
	}

	private Point addPoint(LockDTO lockDTO, Lock lock) {
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
		return point;
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

	public LockResponseDTO updateUserLockSection(long lockId, long sectionId) {
		Lock lock = lockRepository.findOne(lockId);
		LockSection lockSection = lockSectionRepository.findOne(sectionId);
		lock.setLockSection(lockSection);

		return lockConvertor.toResponseDTO(lockRepository.save(lock));
	}

	public LockResponseDTO updateUserLockPaid(long lockId) {
		Lock lock = lockRepository.findOne(lockId);
		lock.setPaid(true);

		return lockConvertor.toResponseDTO(lockRepository.save(lock));
	}
}