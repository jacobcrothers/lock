package binar.box.configuration.storage;

import binar.box.domain.File;
import binar.box.util.Exceptions.FileStorageException;
import com.amazonaws.services.s3.AmazonS3;
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

    @Value("${aws.bucket.category}")
    private String categoryBucket;

    @Value("${aws.bucket.template}")
    private String templateBucket;

    @Value("${aws.bucket.partiallyErasedTemplate}")
    private String partiallyErasedTemplateBucket;

    @Value("${aws.bucket.partiallyErasedTemplateWithText}")
    private String partiallyErasedTemplateWithTextBucket;

    @Value("${aws.bucket.bridge}")
    private String bridgeBucket;

    private AmazonS3 amazonS3;

    public AWSFileStorage(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String store(InputStream file, String key, File.Type type) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        String bucket = getBucketForType(type);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, file, metadata);
        PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);
        log.debug("uploaded file to aws " + key + ", size: " + putObjectResult.getMetadata().getContentLength());
        IOUtils.closeQuietly(file);
        return key;
    }

    @Override
    public InputStream retrieve(String key, File.Type type) throws FileStorageException {
        String bucket = getBucketForType(type);
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);
        S3Object s3Object = amazonS3.getObject(getObjectRequest);
        return s3Object.getObjectContent();
    }

    @Override
    public boolean delete(String key, File.Type type) throws FileStorageException {
        String bucket = getBucketForType(type);
        amazonS3.deleteObject(bucket, key);
        return true; //should we check if the object exists before deleting?
    }

    private String getBucketForType(File.Type type) {
        String bucket;
        switch (type) {
            case CATEGORY:
                bucket = categoryBucket;
                break;
            case FULL_TEMPLATE:
                bucket = templateBucket;
                break;
            case PARTIALY_ERASED_TEMPLATE:
                bucket = partiallyErasedTemplateBucket;
                break;
            case PARTIALY_ERASED_TEMPLATE_WITH_TEXT:
                bucket = partiallyErasedTemplateWithTextBucket;
                break;
            case BRIDGE:
                bucket = bridgeBucket;
                break;
            default:
                bucket = categoryBucket;
        }
        return bucket;
    }
}
