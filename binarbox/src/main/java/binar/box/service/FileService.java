package binar.box.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import binar.box.configuration.storage.FileStorage;
import binar.box.converter.FileConverter;
import binar.box.domain.File;
import binar.box.domain.LockTemplate;
import binar.box.dto.FileDTO;
import binar.box.repository.LockTemplateRepository;
import binar.box.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import binar.box.domain.LockCategory;
import binar.box.repository.FileRepository;
import binar.box.repository.LockCategoryRepository;
import binar.box.util.Constants;
import binar.box.util.Exceptions.LockBridgesException;

@Service
@Transactional
public class FileService {

	private final FileRepository fileRepository;

	private final LockCategoryRepository lockCategoryRepository;

	private final LockTemplateRepository lockTemplateRepository;

	private final FileStorage fileStorage;

	private final FileConverter fileConverter;

	@Value("${file.domain}")
	private String domain;

	@Autowired
	public FileService(FileRepository fileRepository, LockCategoryRepository lockCategoryRepository, LockTemplateRepository lockTemplateRepository, FileStorage fileStorage, FileConverter fileConverter) {
		this.fileRepository = fileRepository;
		this.lockCategoryRepository = lockCategoryRepository;
		this.lockTemplateRepository = lockTemplateRepository;
		this.fileStorage = fileStorage;
		this.fileConverter = fileConverter;
	}

	public void saveFileToLockCategory(MultipartFile file, long lockCategoryId) throws IOException {
		LockCategory lockCategory = lockCategoryRepository.findOne(lockCategoryId);
		createCategoryFile(file, lockCategory);
	}

	private void createCategoryFile(MultipartFile file, LockCategory lockCategory) throws IOException {
			binar.box.domain.File sqlFile = new binar.box.domain.File();
			sqlFile.setFileName(file.getOriginalFilename());

			fileRepository.save(sqlFile);

			String path = fileStorage.store(file.getInputStream(),
					Constants.getFileKey(file.getOriginalFilename(), binar.box.domain.File.Type.CATEGORY, sqlFile.getId()),
					binar.box.domain.File.Type.CATEGORY);
			sqlFile.setPathToFile(path);
			sqlFile.setUrlToFile(Constants.downloadFileUrl(sqlFile.getId(), domain));
			sqlFile.setType(binar.box.domain.File.Type.CATEGORY);

			lockCategory.setFile(fileRepository.save(sqlFile));
	}

	public void saveFilesToLockTemplate(MultipartFile[] files, long lockTemplateId, binar.box.domain.File.Type type) throws IOException {
		LockTemplate lockCategory = lockTemplateRepository.findOne(lockTemplateId);
		createTemplateFiles(files, lockCategory, type);
	}

	private void createTemplateFiles(MultipartFile[] fileList, LockTemplate lockTemplate, binar.box.domain.File.Type type) {
		Arrays.asList(fileList).forEach(file -> {
			binar.box.domain.File sqlFile = new binar.box.domain.File();
			sqlFile.setFileName(file.getOriginalFilename());

			fileRepository.save(sqlFile);

			String path = null;
			try {
				path = fileStorage.store(file.getInputStream(),
						Constants.getFileKey(file.getOriginalFilename(), type, sqlFile.getId()),
						type);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sqlFile.setPathToFile(path);
			sqlFile.setType(type);
			sqlFile.setUrlToFile(Constants.downloadFileUrl(sqlFile.getId(), domain));

			lockTemplate.getFiles().add(fileRepository.save(sqlFile));
		});

		lockTemplateRepository.save(lockTemplate);
	}

	public void saveBridgeFile(MultipartFile file) throws IOException {
		binar.box.domain.File sqlFile = new binar.box.domain.File();
		sqlFile.setFileName(file.getOriginalFilename());

		fileRepository.save(sqlFile);

		String path = fileStorage.store(file.getInputStream(),
				Constants.getFileKey(file.getOriginalFilename(), File.Type.BRIDGE, sqlFile.getId()),
				File.Type.BRIDGE);
		sqlFile.setPathToFile(path);
		sqlFile.setUrlToFile(Constants.downloadFileUrl( sqlFile.getId(), domain));
		sqlFile.setType(binar.box.domain.File.Type.BRIDGE);

		fileRepository.save(sqlFile);
	}

	public InputStream downloadFile(Long fileId) {
		File file = fileRepository.findOne(fileId);

		return fileStorage.retrieve(file.getPathToFile(), file.getType());
	}

	public FileDTO getFile(Long fileId) {
		File file = fileRepository.findOne(fileId);

		return fileConverter.toDTO(file);
	}
}
