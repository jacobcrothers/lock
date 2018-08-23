package binar.box.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import binar.box.domain.LockType;
import binar.box.repository.FileRepository;
import binar.box.repository.LockTypeRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
@Service
@Transactional
public class FileService {

	@Autowired
	private Environment environment;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private LockTypeRepository lockTypeRepository;

	public void saveFilesToLockType(MultipartFile[] files, long lockTypeId) {
		Optional<LockType> lockType = lockTypeRepository.findById(lockTypeId);
		String os = System.getProperty(Constants.OS_NAME).toLowerCase();
		String pathToSaveFiles;
		if (os.contains(Constants.WIN)) {
			pathToSaveFiles = environment.getProperty("filesPath.windows");
		} else {
			pathToSaveFiles = environment.getProperty("filesPath.linux");
		}
		checkOrCreateDirectory(pathToSaveFiles);
		List<File> fileList = saveEachFile(pathToSaveFiles, files);
		createEntityFiles(fileList,
				lockType.orElseThrow(() -> new LockBridgesException(Constants.LOCK_TYPE_NOT_FOUND)));
	}

	private void createEntityFiles(List<File> fileList, LockType lockType) {
		fileList.forEach(file -> {
			binar.box.domain.File sqlFile = new binar.box.domain.File();
			sqlFile.setFileName(file.getName());
			sqlFile.setLockType(lockType);
			sqlFile.setPathToFile(file.getPath());
			sqlFile.setCreatedDate(new Date());
			sqlFile.setLastModifiedDate(new Date());
			fileRepository.save(sqlFile);
		});
	}

	private void checkOrCreateDirectory(String pathToSaveFiles) {
		File directory = new File(pathToSaveFiles);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	private List<File> saveEachFile(String pathToSaveFiles, MultipartFile[] files) {
		List<File> fileList = new ArrayList<>(6);
		for (MultipartFile multipartFile : files) {
			File file = new File(pathToSaveFiles + File.separator + multipartFile.getOriginalFilename());
			try {
				multipartFile.transferTo(file);
			} catch (IOException e) {
				e.printStackTrace();
				throw new LockBridgesException(Constants.EXCEPTION_SAVING_FILES + e.getMessage());
			}
			fileList.add(file);
		}
		return fileList;
	}

	public binar.box.domain.File getFile(long fileId) {
		return fileRepository.findById(fileId).orElseThrow(() -> new LockBridgesException(Constants.FILE_NOT_FOUND));
	}
}
