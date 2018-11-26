package binar.box.configuration.storage;


import binar.box.domain.File;
import binar.box.util.Exceptions.FileStorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for file storage systems. Defines CRUD-like functionality for dealing with file storage
 */
public interface FileStorage {
    String store(InputStream file, String key, File.Type type) throws IOException;

    /**
     * Returns a File instance of the file corresponding to the provided key
     *
     * @param key
     * @return
     */
    InputStream retrieve(String key, File.Type type) throws FileStorageException;

    /**
     * Removes from file storage the file corresponding to the provided key
     *
     * @param key
     */
    boolean delete(String key, File.Type type) throws FileStorageException;
}
