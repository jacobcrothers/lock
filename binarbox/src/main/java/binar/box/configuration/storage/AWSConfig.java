package binar.box.configuration.storage;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {
    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Bean
    public AmazonS3 createAmazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withClientConfiguration(new ClientConfiguration()
                        .withMaxConnections(10000))
                .build();
    }

    //STREAMING

    @Bean
    public AmazonElasticTranscoder createAmazonElasticTranscoderClient() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonElasticTranscoderClient amazonElasticTranscoderClient = new AmazonElasticTranscoderClient(basicAWSCredentials);
        amazonElasticTranscoderClient.setRegion(Region.getRegion(Regions.fromName(region)));
        return amazonElasticTranscoderClient;
    }

    @Bean
    public AmazonSQS createAmazonSQSClient() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonSQSClient amazonSQSClient = new AmazonSQSClient(basicAWSCredentials);
        amazonSQSClient.setRegion(Region.getRegion(Regions.fromName(region)));
        return amazonSQSClient;
    }
}
