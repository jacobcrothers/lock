package binar.box.service;

import binar.box.domain.File;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AmazonS3Service {
    private final Logger log = LoggerFactory.getLogger(AmazonS3Service.class);

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

    @Autowired
    private AmazonS3 amazonS3Client;

    public String uploadFile(MultipartFile multipartFile, File.Type type, Integer fileId) throws IOException {
        String key = generateKey(multipartFile.getOriginalFilename(), type.name(), fileId);
        InputStream inputStream = multipartFile.getInputStream();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        String bucket = getBucketForType(type);

        amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, metadata));
        log.debug("Upload file to AWS " + key);
        IOUtils.closeQuietly(inputStream);
        return key;
    }

    public InputStream downloadFile(String path, File.Type type) {
        String bucket = getBucketForType(type);
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, path);
        S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
        return s3Object.getObjectContent();
    }

    public void deleteFile(String path,File.Type type) {
        String bucket = getBucketForType(type);
        amazonS3Client.deleteObject(bucket, path);
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

    public String generateKey(String originalFileName, String fileType, Integer fileId) {
        return fileType + "_" + fileId + "_" + originalFileName;
    }

}
