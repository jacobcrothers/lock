package binar.box.configuration.storage;


import binar.box.util.Exceptions.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for file storage systems. Defines CRUD-like functionality for dealing with file storage
 */
public interface FileStorage {
    String store(MultipartFile file) throws IOException;

    String store(MultipartFile file, String key) throws IOException;

    /**
     * Returns a File instance of the file corresponding to the provided key
     *
     * @param key
     * @return
     */
    InputStream retrieve(String key) throws FileStorageException;

    /**
     * Removes from file storage the file corresponding to the provided key
     *
     * @param key
     */
    boolean delete(String key) throws FileStorageException;
}
