package binar.box.configuration.storage;

import binar.box.util.Exceptions.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FSFileStorage implements FileStorage {

    private final Logger logger = LoggerFactory.getLogger(FSFileStorage.class);

    @Value("${spring.tjx.file-service.path}")
    private String path;

    @Override
    public String store(MultipartFile file) throws IOException {
        return store(file, file.getOriginalFilename());
    }

    @Override
    public String store(MultipartFile file, String uri) throws IOException {
        File dest = new File(path + File.separator + uri); //todo should we use relative path? will allow migrating com.servustech.tjx.fs folder

        logger.debug("Destination for file: " + dest.getAbsolutePath());

        File parentFolder = dest.getParentFile();

        logger.debug("Parent folder for file: " + parentFolder.getAbsolutePath());
        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        file.transferTo(dest);
        return dest.getAbsolutePath();
    }

    @Override
    public InputStream retrieve(String key) throws FileStorageException {
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
    public boolean delete(String key) throws FileStorageException {
        File file = new File(key);
        if (file.exists()) {
            return file.delete();
        } else {
            throw new FileStorageException("Invalid key provided: " + key);
        }
    }
}
