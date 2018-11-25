package binar.box.configuration.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${file.storageType}")
    private StorageType storageType;

    private AmazonS3 amazonS3;

    @Autowired
    public void setAmazonS3Client(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Bean
    public FileStorage defaultFileStorage() {
        switch (storageType) {
            case AWS_S3:
                return new AWSFileStorage(amazonS3); //make bucket configurable
            default:
                return new FSFileStorage();
        }
    }

}
