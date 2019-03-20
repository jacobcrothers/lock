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
import java.util.stream.Collectors;

@Service
@Transactional
public class FileService {

	private static Map<Integer, Pair> bridgeIntersections= new HashMap<>();

	static
	{
		bridgeIntersections.put(1  ,new Pair(142,573));
		bridgeIntersections.put(2  ,new Pair(145,644));
		bridgeIntersections.put(3  ,new Pair(146,713));
		bridgeIntersections.put(4  ,new Pair(146,784));
		bridgeIntersections.put(5  ,new Pair(182,609));
		bridgeIntersections.put(6  ,new Pair(184,678));
		bridgeIntersections.put(7  ,new Pair(185,748));
		bridgeIntersections.put(8  ,new Pair(184,819));
		bridgeIntersections.put(9  ,new Pair(219,574));
		bridgeIntersections.put(10 ,new Pair(219,643));
		bridgeIntersections.put(11 ,new Pair(223,714));
		bridgeIntersections.put(12 ,new Pair(223,783));
		bridgeIntersections.put(13 ,new Pair(259,608));
		bridgeIntersections.put(14 ,new Pair(262,678));
		bridgeIntersections.put(15 ,new Pair(261,748));
		bridgeIntersections.put(16 ,new Pair(260,819));
		bridgeIntersections.put(17 ,new Pair(294,574));
		bridgeIntersections.put(18 ,new Pair(298,643));
		bridgeIntersections.put(19 ,new Pair(300,713));
		bridgeIntersections.put(20 ,new Pair(299,784));
		bridgeIntersections.put(21 ,new Pair(334,608));
		bridgeIntersections.put(22 ,new Pair(339,678));
		bridgeIntersections.put(23 ,new Pair(336,748));
		bridgeIntersections.put(24 ,new Pair(336,818));
		bridgeIntersections.put(25 ,new Pair(369,574));
		bridgeIntersections.put(26 ,new Pair(372,644));
		bridgeIntersections.put(27 ,new Pair(374,713));
		bridgeIntersections.put(28 ,new Pair(374,783));
		bridgeIntersections.put(29 ,new Pair(407,610));
		bridgeIntersections.put(30 ,new Pair(409,679));
		bridgeIntersections.put(31 ,new Pair(411,748));
		bridgeIntersections.put(32 ,new Pair(413,818));
		bridgeIntersections.put(33 ,new Pair(442,573));
		bridgeIntersections.put(34 ,new Pair(445,644));
		bridgeIntersections.put(35 ,new Pair(448,713));
		bridgeIntersections.put(36 ,new Pair(451,783));
		bridgeIntersections.put(37 ,new Pair(482,608));
		bridgeIntersections.put(38 ,new Pair(483,678));
		bridgeIntersections.put(39 ,new Pair(458,748));
		bridgeIntersections.put(40 ,new Pair(486,818));
		bridgeIntersections.put(41 ,new Pair(518,574));
		bridgeIntersections.put(42 ,new Pair(522,643));
		bridgeIntersections.put(43 ,new Pair(521,714));
		bridgeIntersections.put(44 ,new Pair(522,783));
		bridgeIntersections.put(45 ,new Pair(557,609));
		bridgeIntersections.put(46 ,new Pair(558,678));
		bridgeIntersections.put(47 ,new Pair(557,749));
		bridgeIntersections.put(48 ,new Pair(558,818));
		bridgeIntersections.put(49 ,new Pair(589,574));
		bridgeIntersections.put(50 ,new Pair(593,643));
		bridgeIntersections.put(51 ,new Pair(595,713));
		bridgeIntersections.put(52 ,new Pair(595,783));
		bridgeIntersections.put(53 ,new Pair(628,609));
		bridgeIntersections.put(54 ,new Pair(631,678));
		bridgeIntersections.put(55 ,new Pair(632,748));
		bridgeIntersections.put(56 ,new Pair(631,818));
		bridgeIntersections.put(57 ,new Pair(663,573));
		bridgeIntersections.put(58 ,new Pair(667,644));
		bridgeIntersections.put(59 ,new Pair(671,714));
		bridgeIntersections.put(60 ,new Pair(669,783));
		bridgeIntersections.put(61 ,new Pair(701,608));
		bridgeIntersections.put(62 ,new Pair(705,678));
		bridgeIntersections.put(63 ,new Pair(707,748));
		bridgeIntersections.put(64 ,new Pair(708,819));
		bridgeIntersections.put(65 ,new Pair(736,573));
		bridgeIntersections.put(66 ,new Pair(740,645));
		bridgeIntersections.put(67 ,new Pair(742,713));
		bridgeIntersections.put(68 ,new Pair(744,784));
		bridgeIntersections.put(69 ,new Pair(776,608));
		bridgeIntersections.put(70 ,new Pair(778,678));
		bridgeIntersections.put(71 ,new Pair(780,748));
		bridgeIntersections.put(72 ,new Pair(782,817));
		bridgeIntersections.put(73 ,new Pair(812,573));
		bridgeIntersections.put(74 ,new Pair(816,644));
		bridgeIntersections.put(75 ,new Pair(816,713));
		bridgeIntersections.put(76 ,new Pair(818,783));
		bridgeIntersections.put(77 ,new Pair(851,609));
		bridgeIntersections.put(78 ,new Pair(855,678));
		bridgeIntersections.put(79 ,new Pair(856,748));
		bridgeIntersections.put(80 ,new Pair(855,819));
		bridgeIntersections.put(81 ,new Pair(886,574));
		bridgeIntersections.put(82 ,new Pair(889,643));
		bridgeIntersections.put(83 ,new Pair(891,713));
		bridgeIntersections.put(84 ,new Pair(892,784));
		bridgeIntersections.put(85 ,new Pair(924,609));
		bridgeIntersections.put(86 ,new Pair(925,679));
		bridgeIntersections.put(87 ,new Pair(927,749));
		bridgeIntersections.put(88 ,new Pair(927,818));
		bridgeIntersections.put(89 ,new Pair(958,574));
		bridgeIntersections.put(90 ,new Pair(960,643));
		bridgeIntersections.put(91 ,new Pair(963,714));
		bridgeIntersections.put(92 ,new Pair(964,785));
		bridgeIntersections.put(93 ,new Pair(995,608));
		bridgeIntersections.put(94 ,new Pair(998,678));
		bridgeIntersections.put(95 ,new Pair(998,750));
		bridgeIntersections.put(96 ,new Pair(999,818));
		bridgeIntersections.put(97 ,new Pair(1112,576));
		bridgeIntersections.put(98 ,new Pair(1114,646));
		bridgeIntersections.put(99 ,new Pair(1115,716));
		bridgeIntersections.put(100,new Pair(1116,786));
		bridgeIntersections.put(101,new Pair(1151,611));
		bridgeIntersections.put(102,new Pair(1153,680));
		bridgeIntersections.put(103,new Pair(1153,751));
		bridgeIntersections.put(104,new Pair(1154,821));
		bridgeIntersections.put(105,new Pair(1188,576));
		bridgeIntersections.put(106,new Pair(1190,645));
		bridgeIntersections.put(107,new Pair(1191,716));
		bridgeIntersections.put(108,new Pair(1191,786));
		bridgeIntersections.put(109,new Pair(1227,610));
		bridgeIntersections.put(110,new Pair(1229,681));
		bridgeIntersections.put(111,new Pair(1229,751));
		bridgeIntersections.put(112,new Pair(1228,821));
		bridgeIntersections.put(113,new Pair(1264,576));
		bridgeIntersections.put(114,new Pair(1266,646));
		bridgeIntersections.put(115,new Pair(1268,716));
		bridgeIntersections.put(116,new Pair(1267,785));
		bridgeIntersections.put(117,new Pair(1303,611));
		bridgeIntersections.put(118,new Pair(1306,680));
		bridgeIntersections.put(119,new Pair(1305,750));
		bridgeIntersections.put(120,new Pair(1305,821));
		bridgeIntersections.put(121,new Pair(1338,576));
		bridgeIntersections.put(122,new Pair(1341,646));
		bridgeIntersections.put(123,new Pair(1342,716));
		bridgeIntersections.put(124,new Pair(1342,785));
		bridgeIntersections.put(125,new Pair(1376,611));
		bridgeIntersections.put(126,new Pair(1377,681));
		bridgeIntersections.put(127,new Pair(1379,750));
		bridgeIntersections.put(128,new Pair(1380,820));
		bridgeIntersections.put(129,new Pair(1411,576));
		bridgeIntersections.put(130,new Pair(1414,646));
		bridgeIntersections.put(131,new Pair(1415,716));
		bridgeIntersections.put(132,new Pair(1419,785));
		bridgeIntersections.put(133,new Pair(1452,611));
		bridgeIntersections.put(134,new Pair(1453,680));
		bridgeIntersections.put(135,new Pair(1453,751));
		bridgeIntersections.put(136,new Pair(1455,820));
		bridgeIntersections.put(137,new Pair(1486,575));
		bridgeIntersections.put(138,new Pair(1491,646));
		bridgeIntersections.put(139,new Pair(1489,716));
		bridgeIntersections.put(140,new Pair(1490,785));
		bridgeIntersections.put(141,new Pair(1525,610));
		bridgeIntersections.put(142,new Pair(1527,681));
		bridgeIntersections.put(143,new Pair(1525,751));
		bridgeIntersections.put(144,new Pair(1527,821));
		bridgeIntersections.put(145,new Pair(1560,576));
		bridgeIntersections.put(146,new Pair(1562,646));
		bridgeIntersections.put(147,new Pair(1562,716));
		bridgeIntersections.put(148,new Pair(1562,785));
		bridgeIntersections.put(149,new Pair(1596,610));
		bridgeIntersections.put(150,new Pair(1600,680));
		bridgeIntersections.put(151,new Pair(1601,751));
		bridgeIntersections.put(152,new Pair(1600,821));
		bridgeIntersections.put(153,new Pair(1632,576));
		bridgeIntersections.put(154,new Pair(1636,646));
		bridgeIntersections.put(155,new Pair(1639,716));
		bridgeIntersections.put(156,new Pair(1638,785));
		bridgeIntersections.put(157,new Pair(1671,611));
		bridgeIntersections.put(158,new Pair(1674,681));
		bridgeIntersections.put(159,new Pair(1675,751));
		bridgeIntersections.put(160,new Pair(1676,821));
		bridgeIntersections.put(161,new Pair(1706,577));
		bridgeIntersections.put(162,new Pair(1709,647));
		bridgeIntersections.put(163,new Pair(1711,716));
		bridgeIntersections.put(164,new Pair(1712,785));
		bridgeIntersections.put(165,new Pair(1746,611));
		bridgeIntersections.put(166,new Pair(1747,680));
		bridgeIntersections.put(167,new Pair(1747,751));
		bridgeIntersections.put(168,new Pair(1749,821));
		bridgeIntersections.put(169,new Pair(1781,577));
		bridgeIntersections.put(170,new Pair(1785,646));
		bridgeIntersections.put(171,new Pair(1786,716));
		bridgeIntersections.put(172,new Pair(1786,785));
		bridgeIntersections.put(173,new Pair(1820,611));
		bridgeIntersections.put(174,new Pair(1823,680));
		bridgeIntersections.put(175,new Pair(1824,751));
		bridgeIntersections.put(176,new Pair(1823,821));
		bridgeIntersections.put(177,new Pair(1856,577));
		bridgeIntersections.put(178,new Pair(1859,645));
		bridgeIntersections.put(179,new Pair(1860,716));
		bridgeIntersections.put(180,new Pair(1860,786));
		bridgeIntersections.put(181,new Pair(1894,611));
		bridgeIntersections.put(182,new Pair(1895,681));
		bridgeIntersections.put(183,new Pair(1896,752));
		bridgeIntersections.put(184,new Pair(1896,821));
		bridgeIntersections.put(185,new Pair(1928,576));
		bridgeIntersections.put(186,new Pair(1930,646));
		bridgeIntersections.put(187,new Pair(1931,716));
		bridgeIntersections.put(188,new Pair(1932,786));
		bridgeIntersections.put(189,new Pair(1965,610));
		bridgeIntersections.put(190,new Pair(1966,681));
		bridgeIntersections.put(191,new Pair(1968,753));
		bridgeIntersections.put(192,new Pair(1968,821));

//		List<Integer> yCoords= Arrays.asList(574,
//				610,
//				644,
//				679,
//				715,
//				750,
//				785,
//				820);


	}

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
			// information provided by Facebook@
			startOffset = filePart.getStartOffset();
			endOffset = filePart.getEndOffset();
			length = endOffset - startOffset;
		}
	}

	public InputStream downloadBridgeFile() throws IOException {

		long start = System.nanoTime();
        BufferedImage bridgePicBuffered = getBufferedImageFromAmazon(BRIDGE_ID);

		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.printf("Processed aws download in %d millis\n", duration);
        List<Long> lockWithTextIds = fileRepository.getFilesIdByType(File.Type.PARTIALY_ERASED_TEMPLATE_WITH_TEXT.ordinal())
				.stream().map(BigInteger::longValue).collect(Collectors.toList());

		long durationDb = (System.nanoTime() - start) / 1_000_000;
		System.out.printf("Processed db in %d millis\n", durationDb);

        List<Image> rescaledLocks = lockWithTextIds.parallelStream().map(this::getBufferedImageFromAmazon).collect(Collectors.toList());

		long durationRescale = (System.nanoTime() - start) / 1_000_000;
		System.out.printf("Processed rescale in %d millis\n", durationRescale);

        Graphics2D g = bridgePicBuffered.createGraphics();

        for (int i=1; i< bridgeIntersections.size(); i+=5) {
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
		g.drawImage(rescaledLocks.get(0), bridgeIntersections.get(i).getX()-216, bridgeIntersections.get(i).getY()-85, null);
	}

//	private Image getRescaledImageFromAmazon(Long pictureId) {
//		BufferedImage img = getBufferedImageFromAmazon(pictureId);
//		return img.getScaledInstance(420, 300, Image.SCALE_SMOOTH);
//    }

	private BufferedImage getBufferedImageFromAmazon(long pictureId) {
		InputStream bridgePicInputStream = downloadFile(pictureId);

		BufferedImage img = null;
		try {
			img = ImageIO.read(bridgePicInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
}
