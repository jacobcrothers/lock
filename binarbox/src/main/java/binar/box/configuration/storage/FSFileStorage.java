package binar.box.configuration.storage;

import binar.box.util.Exceptions.FileStorageException;
import binar.box.util.ImageUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FSFileStorage implements FileStorage {

    private final Logger logger = LoggerFactory.getLogger(FSFileStorage.class);

    @Override
    public String store(InputStream file, String uri, binar.box.domain.File.Type type) throws IOException {
        File dest = new File(ImageUtils.returnPathToImages() + File.separator + type); //todo should we use relative path? will allow migrating com.servustech.tjx.fs folder

        logger.debug("Destination for file: " + dest.getAbsolutePath());

        File parentFolder = dest.getParentFile();

        logger.debug("Parent folder for file: " + parentFolder.getAbsolutePath());
        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        FileUtils.copyInputStreamToFile(file, dest);
        return dest.getAbsolutePath();
    }

    @Override
    public InputStream retrieve(String key, binar.box.domain.File.Type type) throws FileStorageException {
        File file = new File(key);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new FileStorageException("Unable to create FileInputStream for " + key, e);
            }
        } else {
            throw new FileStorageException("Invalid key provided: " + key);
        }
    }

    @Override
    public boolean delete(String key, binar.box.domain.File.Type type) throws FileStorageException {
        File file = new File(key);
        if (file.exists()) {
            return file.delete();
        } else {
            throw new FileStorageException("Invalid key provided: " + key);
        }
    }
}
