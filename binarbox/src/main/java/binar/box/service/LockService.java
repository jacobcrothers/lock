package binar.box.service;

import binar.box.configuration.storage.FileStorage;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static binar.box.util.ImageUtils.readImageFromURL;

@Service
@Transactional
public class LockService {

	private final EmailService emailService;

	private final LockCategoryRepository lockCategoryRepository;

	private final LockSectionRepository lockSectionRepository;

	private final UserService userService;

	private final LockRepository lockRepository;

	private final LockCategoryConverter lockCategoryConverter;

	private final LockConvertor lockConvertor;

	private final LockTemplateRepository lockTemplateRepository;

	private final LockSectionConvertor lockSectionConvertor;

	private final PriceRepository priceRepository;

	private final PointRepository pointRepository;

	private final FileStorage fileStorage;

	private final FileRepository fileRepository;

	@Value("${file.domain}")
	private String domain;

	@Autowired
	public LockService(LockSectionRepository lockSectionRepository, EmailService emailService, LockCategoryRepository lockCategoryRepository, UserService userService, FileStorage fileStorage, LockRepository lockRepository, LockCategoryConverter lockCategoryConverter, LockConvertor lockConvertor, LockTemplateRepository lockTemplateRepository, LockSectionConvertor lockSectionConvertor, FileRepository fileRepository, PriceRepository priceRepository, PointRepository pointRepository) {
		this.lockSectionRepository = lockSectionRepository;
		this.emailService = emailService;
		this.lockCategoryRepository = lockCategoryRepository;
		this.userService = userService;
		this.fileStorage = fileStorage;
		this.lockRepository = lockRepository;
		this.lockCategoryConverter = lockCategoryConverter;
		this.lockConvertor = lockConvertor;
		this.lockTemplateRepository = lockTemplateRepository;
		this.lockSectionConvertor = lockSectionConvertor;
		this.fileRepository = fileRepository;
		this.priceRepository = priceRepository;
		this.pointRepository = pointRepository;
	}

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
//		if (Objects.isNull(lockStepOneDTO.getLockImageWithText()))
//
//			saveTextOnImage(lock);
//		else {
			saveTextImageSent(lock);
//		}
        return lockConvertor.toStepOneDTO(lockRepository.save(lock));
    }

    @Deprecated
	public LockResponseDTO createUserLock(LockDTO lockDTO) throws IOException {
		Lock lock = populateEntity(lockDTO, new Lock());
		lockRepository.save(lock);
		saveTextOnImage(lock);
		return lockConvertor.toResponseDTO(lockRepository.save(lock));
	}

	public LockResponseDTO updateUserLock(LockDTO lockDTO){
		if (Objects.isNull(lockDTO.getId()))
			throw new LockBridgesException(Constants.LOCK_NOT_FOUND,"lock.not.found");
		Lock lock = lockRepository.findOne(lockDTO.getId());
		Lock updatedLock = populateEntity(lockDTO, lock);
		updatedLock.setId(lockDTO.getId());

		return lockConvertor.toResponseDTO(lockRepository.save(updatedLock));
	}

	public List<LockResponseDTO> getUnpaidLocks() {
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
			throw new LockBridgesException(Constants.LOCK_NOT_FOUND, "lock.not.found");
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

	private void saveTextOnImage(Lock lock) throws IOException {
		File lockFile =  lock.getLockTemplate().getFiles().stream()
				.filter(f -> f.getType().equals(File.Type.PARTIALY_ERASED_TEMPLATE))
				.findAny()
				.orElseThrow(() -> new LockBridgesException("Lock partialy erased image not found","partial.lock.not.found"));

		InputStream storageFile = fileStorage.retrieve(lockFile.getPathToFile(), File.Type.PARTIALY_ERASED_TEMPLATE);
		InputStream imageWithText = ImageUtils.addTextToImage(storageFile, lock.getMessage());
		File sqlFile = storeFile(lockFile.getFileName(), imageWithText);

		lock.getFiles().add(fileRepository.save(sqlFile));
//		TODO: Add glitter file
	}

	private void saveTextImageSent(Lock lock) throws IOException {
		File lockFile =  lock.getLockTemplate().getFiles().stream()
				.filter(f -> f.getType().equals(File.Type.PARTIALY_ERASED_TEMPLATE))
				.findAny()
				.orElseThrow(() -> new LockBridgesException("Lock partialy erased image not found","partial.lock.not.found"));
		Long templateId = lockFile.getId();

		String message = lock.getMessage();

		String url = "http://localhost:8080/api/v1/generateImage?font=Arial&fontSize=12&message=" +
				message +
				"&templateId=" +
				templateId +
				"&color=%23FF99DD";
		InputStream lockWithTextFromURL = readImageFromURL(url);

		File sqlFile = storeFile(lockFile.getFileName(), lockWithTextFromURL);
//		File sqlFile = storeFile(lockFile.getFileName(), lockImage.getInputStream());

		lock.getFiles().add(fileRepository.save(sqlFile));

		lockRepository.save(lock);
//		TODO: Add glitter file
	}

	private File storeFile(String lockFileName, InputStream imageWithText) throws IOException {
		File sqlFile = new File();
		sqlFile.setFileName(lockFileName);


		fileRepository.save(sqlFile);

		String path = fileStorage.store(imageWithText,
				Constants.getFileKey(lockFileName, File.Type.PARTIALY_ERASED_TEMPLATE_WITH_TEXT, sqlFile.getId()),
				File.Type.PARTIALY_ERASED_TEMPLATE_WITH_TEXT);
		sqlFile.setPathToFile(path);
		sqlFile.setUrlToFile(Constants.downloadFileUrl(sqlFile.getId(), domain));
		sqlFile.setType(File.Type.PARTIALY_ERASED_TEMPLATE_WITH_TEXT);
		return sqlFile;
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

	public Lock get(Long lockId) {
		return lockRepository.findOne(lockId);
	}
}
