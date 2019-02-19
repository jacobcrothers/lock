package binar.box.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.transaction.Transactional;

import binar.box.configuration.storage.FileStorage;
import binar.box.converter.FileConverter;
import binar.box.domain.File;
import binar.box.domain.LockTemplate;
import binar.box.domain.User;
import binar.box.dto.FileDTO;
import binar.box.repository.LockTemplateRepository;
import com.restfb.*;
import com.restfb.types.GraphResponse;
import com.restfb.types.ResumableUploadStartResponse;
import com.restfb.types.ResumableUploadTransferResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import binar.box.domain.LockCategory;
import binar.box.repository.FileRepository;
import binar.box.repository.LockCategoryRepository;
import binar.box.util.Constants;

@Service
@Transactional
public class FileService {

	private final FileRepository fileRepository;

	private final LockCategoryRepository lockCategoryRepository;

	private final LockTemplateRepository lockTemplateRepository;

	private final FileStorage fileStorage;

	private final FileConverter fileConverter;

	private final UserService userService;

	@Value("${file.domain}")
	private String domain;

	@Autowired
	public FileService(FileRepository fileRepository, LockCategoryRepository lockCategoryRepository, LockTemplateRepository lockTemplateRepository, FileStorage fileStorage, FileConverter fileConverter, UserService userService) {
		this.fileRepository = fileRepository;
		this.lockCategoryRepository = lockCategoryRepository;
		this.lockTemplateRepository = lockTemplateRepository;
		this.fileStorage = fileStorage;
		this.fileConverter = fileConverter;
		this.userService = userService;
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
		sqlFile.setUrlToFile(Constants.downloadFileUrl(sqlFile.getId(), domain));
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

	public String uploadVideo(MultipartFile video) throws IOException {
		var facebook = new FacebookTemplate(userService.getAuthenticatedUserToken());

		String result = facebook.mediaOperations().postVideo(getUploadResource(video.getOriginalFilename(), video), "Lock bridge", "romantic description");
		return result;
	}

	private Resource getUploadResource(final String filename, MultipartFile video) throws IOException {
		return new ByteArrayResource(video.getBytes()) {
			public String getFilename() throws IllegalStateException {
				return filename;
			}
		};
	}

	public String uploadVideoWithFbRest(MultipartFile video) throws IOException {
		long filesizeInBytes = video.getSize();
		InputStream in =  new BufferedInputStream(video.getInputStream());

		// We need the file size in bytes to make the start request
		FacebookClient fbc = new DefaultFacebookClient(userService.getAuthenticatedUserToken(), Version.LATEST);
		String connection = "/" + userService.getAuthenticatedUser().getId() + "/videos";
		ResumableUploadStartResponse returnValue = fbc.publish(connection,
				ResumableUploadStartResponse.class, // The return value
				Parameter.with("upload_phase", "start"), // The upload phase
				Parameter.with("file_size", filesizeInBytes)); // The file size

		long startOffset = returnValue.getStartOffset();
		long endOffset = returnValue.getEndOffset();
		long length = endOffset - startOffset;

		// The upload session ID is very important, because Facebook needs
		// this ID to identify all the uploads that belong together
		String uploadSessionId = returnValue.getUploadSessionId();

		transferPhase(in, fbc, startOffset, length, uploadSessionId);

		GraphResponse finishResponse = fbc.publish("/videos",
				GraphResponse.class,
				Parameter.with("upload_phase", "finish"), // Tell Facebook to finish the upload
				Parameter.with("upload_session_id", uploadSessionId)); // The corresponding session ID

		return "succes";
	}

	private void transferPhase(InputStream in, FacebookClient fbc, long startOffset, long length, String uploadSessionId) throws IOException {
		long endOffset;// We have to upload the chunks in a loop
		while (length > 0) {
			// First copy bytes in byte array
			byte[] fileBytes = new byte[Math.toIntExact(length)];
			in.read(fileBytes);

			// Make the request to Facebook
			ResumableUploadTransferResponse filePart = fbc.publish("PAGE_ID/videos",
					// The returned object
					ResumableUploadTransferResponse.class,
					// The file chunk that should be uploaded now
					BinaryAttachment.with("video_file_chunk", fileBytes),
					// Tell Facebook that we are in the transfer phase now
					Parameter.with("upload_phase", "transfer"),
					// The offset the file chunk starts
					Parameter.with("start_offset", startOffset),
					// The important session ID of this file transfer
					Parameter.with("upload_session_id", uploadSessionId));

			// After uploading the chunk we recalculate the offsets according to the
			// information provided by Facebook
			startOffset = filePart.getStartOffset();
			endOffset = filePart.getEndOffset();
			length = endOffset - startOffset;
		}
	}
}
