package binar.box.service;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import binar.box.util.Exceptions.EntityNotFoundException;
import binar.box.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import binar.box.domain.LockType;
import binar.box.repository.FileRepository;
import binar.box.repository.LockTypeRepository;
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
	private LockTypeRepository lockTypeRepository;

	public void saveFilesToLockType(MultipartFile[] files, long lockTypeId) throws IOException {
		LockType lockType = lockTypeRepository.findOne(lockTypeId);
		String os = System.getProperty(Constants.OS_NAME).toLowerCase();
		String pathToSaveFiles;
		if (os.contains(Constants.WIN)) {
			pathToSaveFiles = environment.getProperty("filesPath.windows");
		} else {
			pathToSaveFiles = environment.getProperty("filesPath.linux");
		}
		checkOrCreateDirectory(pathToSaveFiles);
		List<File> fileList = saveEachFile(pathToSaveFiles, files);
		createEntityFiles(fileList, lockType);
	}

	private void createEntityFiles(List<File> fileList, LockType lockType) {
		fileList.forEach(file -> {
			binar.box.domain.File sqlFile = new binar.box.domain.File();
			sqlFile.setFileName(file.getName());
			sqlFile.setLockType(lockType);
			sqlFile.setPathToFile(file.getPath());
			fileRepository.save(sqlFile);
		});
	}

	private void checkOrCreateDirectory(String pathToSaveFiles) {
		File directory = new File(pathToSaveFiles);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	private List<File> saveEachFile(String pathToSaveFiles, MultipartFile[] files) throws IOException {
		List<File> fileList = new ArrayList<>(6);
		for (MultipartFile multipartFile : files) {
			File file = new File(pathToSaveFiles + File.separator + multipartFile.getOriginalFilename());
			try {
				addTextToImage(multipartFile, pathToSaveFiles);
				multipartFile.transferTo(file);
			} catch (IOException e) {
				e.printStackTrace();
				throw new LockBridgesException(Constants.EXCEPTION_SAVING_FILES + e.getMessage());
			}
			fileList.add(file);
		}
		return fileList;
	}

    private void addTextToImage(MultipartFile multipartFile, String pathToSaveFiles) throws IOException {

        BufferedImage file2buffer = ImageUtils.convertToImage(multipartFile);

        Graphics graphics = file2buffer.getGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, 50, 50);
        graphics.setColor(Color.BLUE);
        graphics.setFont(new Font("Arial Black", Font.BOLD, 20));
        graphics.drawString("AWESOME", 10, 25);

		graphics.setFont(graphics.getFont().deriveFont(30f));
		graphics.drawString("Hello World!", 100, 100);
		graphics.dispose();

		Graphics2D w = (Graphics2D) file2buffer.getGraphics();
		w.drawImage(file2buffer, 0, 0, null);
		AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
		w.setComposite(alphaChannel);
		w.setColor(Color.GREEN);
		w.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
		FontMetrics fontMetrics = w.getFontMetrics();
		Rectangle2D rect = fontMetrics.getStringBounds("this is sooo good", w);

		// calculate center of the image
		int centerX = (file2buffer.getWidth() - (int) rect.getWidth()) / 2;
		int centerY = file2buffer.getHeight() / 2;

		// add text overlay to the image
		w.drawString("this is sooo good", centerX, centerY);

		w.dispose();

        ImageIO.write(file2buffer, "png", new File(
				pathToSaveFiles + File.separator + "New" + multipartFile.getOriginalFilename()));
    }

    public binar.box.domain.File getFile(long fileId) {
		return fileRepository.findOne(fileId);
	}
}
