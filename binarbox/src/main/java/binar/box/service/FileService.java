package binar.box.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import binar.box.configuration.storage.FileStorage;
import binar.box.converter.FileConverter;
import binar.box.domain.File;
import binar.box.domain.LockTemplate;
import binar.box.domain.Point;
import binar.box.dto.FileDTO;
import binar.box.dto.PointDTO;
import binar.box.dto.points.Pair;
import binar.box.repository.LockTemplateRepository;
import binar.box.util.ImageUtils;
import binar.box.util.IntersectionUtil;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;

import static binar.box.domain.File.Type.PARTIALY_ERASED_TEMPLATE_WITH_TEXT;

@Service
@Transactional
public class FileService {



	private static final long BRIDGE_ID = 4L;
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

	public InputStream downloadFile(Long fileId) throws IOException {
		File file = fileRepository.findOne(fileId);

		return fileStorage.retrieve(file.getPathToFile(), PARTIALY_ERASED_TEMPLATE_WITH_TEXT);
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
			// information provided by Facebook@
			startOffset = filePart.getStartOffset();
			endOffset = filePart.getEndOffset();
			length = endOffset - startOffset;
		}
	}


	public InputStream downloadBridgeFile() throws IOException, ExecutionException, InterruptedException {
		//transform to jpeg
		//read from disk
		//investigate why size gets so large
		//png on png action
		long start = System.nanoTime();

        BufferedImage bridgePicBuffered = getBufferedImageFromAmazon(fileRepository.findOne(BRIDGE_ID));

		long duration = (System.nanoTime() - start) / 1_000_000;

//        List<Long> lockWithTextIds = fileRepository.getFilesIdByType(PARTIALY_ERASED_TEMPLATE_WITH_TEXT.ordinal())
//				.stream().map(BigInteger::longValue).collect(Collectors.toList());
		List<File> filePaths = fileRepository.getFilesPathByType(PARTIALY_ERASED_TEMPLATE_WITH_TEXT.ordinal());

		System.out.printf("Processed aws download in %d millis\n", duration);

		File test = filePaths.get(0);
		for (int i=0;i<200;i++) {
			filePaths.add(test);
		}

		long durationDb = (System.nanoTime() - start) / 1_000_000;
		System.out.printf("Processed db in %d millis\n", durationDb);


		ForkJoinPool ioPool = new ForkJoinPool(filePaths.size());
		ForkJoinTask<List<Image>> tasks = ioPool.submit(
				() -> filePaths.parallelStream().map(this::getBufferedImageFromAmazon).collect(Collectors.toList()));
		List<Image> rescaledLocks = tasks.get();

		long durationRescale = (System.nanoTime() - start) / 1_000_000;
		System.out.printf("Get aws lock pics %d millis\n", durationRescale);

        Graphics2D g = bridgePicBuffered.createGraphics();

        for (int i = 1; i< IntersectionUtil.bridgeIntersections.size()- 6; i+=15) {
        	drawLockOnPanel(rescaledLocks, g, i);
		}

		long durationDraw = (System.nanoTime() - start) / 1_000_000;
		System.out.printf("Processed draw in %d millis\n", durationDraw);
		return ImageUtils.BufferedImageToInputStream(bridgePicBuffered);

//		ImageUtils.writeImage(bridgePicBuffered, ImageUtils.returnPathToImages() + java.io.File.separator + "lockBridge.png","PNG");
//
//		long durationWrite = (System.nanoTime() - start) / 1_000_000;
//		System.out.printf("Processed write in %d millis\n", durationWrite);
	}

	private void drawLockOnPanel(List<Image> rescaledLocks, Graphics2D g, int i) {
		g.drawImage(rescaledLocks.get(0),
				IntersectionUtil.bridgeIntersections.get(i).getX()-216,
				IntersectionUtil.bridgeIntersections.get(i).getY()-85,
				null);
	}

//	private Image getRescaledImageFromAmazon(Long pictureId) {
//		BufferedImage img = getBufferedImageFromAmazon(pictureId);
//		return img.getScaledInstance(420, 300, Image.SCALE_SMOOTH);
//    }

	private BufferedImage getBufferedImageFromAmazon(File file) {
		BufferedImage img = null;
		try {
			InputStream bridgePicInputStream = fileStorage.retrieve(file.getPathToFile(), file.getType());
			img = ImageIO.read(bridgePicInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
}
