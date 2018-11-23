package binar.box.configuration.storage;

import binar.box.util.Exceptions.FileStorageException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class AWSFileStorage implements FileStorage {

    private final Logger log = LoggerFactory.getLogger(AWSFileStorage.class);

    @Value("${cloud.tjx.aws.bucket}")
    private String bucket;

    private AmazonS3Client amazonS3Client;

    public AWSFileStorage(AmazonS3Client amazonS3Client, String bucket) {
        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        return store(file, file.getOriginalFilename());
    }

    @Override
    public String store(MultipartFile file, String key) throws IOException {
        InputStream sourceFileIS = file.getInputStream();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, sourceFileIS, metadata);
        PutObjectResult putObjectResult = amazonS3Client.putObject(putObjectRequest);
        log.debug("uploaded file to aws " + key + ", size: " + putObjectResult.getMetadata().getContentLength());
        IOUtils.closeQuietly(sourceFileIS);
        return key;
    }

    @Override
    public InputStream retrieve(String key) throws FileStorageException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);
        S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
        return s3Object.getObjectContent();
    }

    @Override
    public boolean delete(String key) throws FileStorageException {
        amazonS3Client.deleteObject(bucket, key);
        return true; //should we check if the object exists before deleting?
    }
}
