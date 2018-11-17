package binar.box.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import binar.box.domain.LockTemplate;
import binar.box.repository.LockTemplateRepository;
import binar.box.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private Environment environment;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private LockCategoryRepository lockCategoryRepository;

	@Autowired
	private LockTemplateRepository lockTemplateRepository;

	public void saveFilesToLockCategory(MultipartFile file, long lockCategoryId) throws IOException {
		LockCategory lockCategory = lockCategoryRepository.findOne(lockCategoryId);
		File diskFile = storeFile(checkOrCreateDirectory(), file);
		createCategoryFile(diskFile, lockCategory);
	}

	private List<File> saveFilesOnDisk(MultipartFile[] files) throws IOException {
		return saveEachFile(checkOrCreateDirectory(), files);
	}

	private void createCategoryFile(File file, LockCategory lockCategory) {
			binar.box.domain.File sqlFile = new binar.box.domain.File();
			sqlFile.setFileName(file.getName());
			sqlFile.setPathToFile(file.getPath());
			sqlFile.setType(binar.box.domain.File.Type.CATEGORY);

			lockCategory.setFile(fileRepository.save(sqlFile));
	}

	private String checkOrCreateDirectory() {
		File directory = new File(ImageUtils.returnPathToImages());
		if (!directory.exists()) {
			directory.mkdirs();
		}
		return directory.getAbsolutePath();
	}

	private List<File> saveEachFile(String pathToSaveFiles, MultipartFile[] files) throws IOException {
		List<File> fileList = new ArrayList<>(6);
		for (MultipartFile multipartFile : files) {
            File file = storeFile(pathToSaveFiles, multipartFile);
            fileList.add(file);
		}
		return fileList;
	}

    private File storeFile(String pathToSaveFiles, MultipartFile multipartFile) {
        File file = new File(pathToSaveFiles + File.separator + multipartFile.getOriginalFilename());
        try {
//				addTextToImage(multipartFile, pathToSaveFiles);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LockBridgesException(Constants.EXCEPTION_SAVING_FILES + e.getMessage());
        }
        return file;
    }

    public binar.box.domain.File getFile(long fileId) {
		return fileRepository.findOne(fileId);
	}

	public void saveFilesToLockTemplate(MultipartFile[] files, long lockTemplateId, binar.box.domain.File.Type type) throws IOException {
		LockTemplate lockCategory = lockTemplateRepository.findOne(lockTemplateId);
		List<File> fileList = saveFilesOnDisk(files);
		createTemplateFiles(fileList, lockCategory, type);
	}

	private void createTemplateFiles(List<File> fileList, LockTemplate lockTemplate, binar.box.domain.File.Type type) {
		fileList.forEach(file -> {
			binar.box.domain.File sqlFile = new binar.box.domain.File();
			sqlFile.setFileName(file.getName());
			sqlFile.setPathToFile(file.getPath());
			sqlFile.setType(type);

			lockTemplate.getFiles().add(fileRepository.save(sqlFile));
		});

		lockTemplateRepository.save(lockTemplate);
	}
}
