package binar.box.configuration.storage;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${spring.tjx.file-service.storage-type}")
    private StorageType storageType;

    private AmazonS3Client amazonS3Client;

    @Autowired
    public void setAmazonS3Client(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Bean
    public FileStorage defaultFileStorage() {
        switch (storageType) {
            case AWS_S3:
                return new AWSFileStorage(amazonS3Client, "eppione"); //make bucket configurable
            default:
                return new FSFileStorage();
        }
    }

}
