package binar.box.configuration.storage;

import binar.box.configuration.storage.utils.JobStatusNotification;
import binar.box.configuration.storage.utils.JobStatusNotificationHandler;
import binar.box.configuration.storage.utils.SqsQueueNotificationWorker;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder;
import com.amazonaws.services.elastictranscoder.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class AWSVideoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AWSVideoService.class);

    @Autowired
    private AmazonS3 amazonS3Client;

    @Autowired
    private AmazonElasticTranscoder amazonElasticTranscoderClient;

    @Autowired
    private SqsQueueNotificationWorker sqsQueueNotificationWorker;

    @Value("${aws.s3.url}")
    private String awsS3Url;

    @Value("${aws.encoding.pipeline.id}")
    private String awsEncodingPipelineId;

    @Value("${aws.system.preset.hls.400k.id}")
    private String awsSystemPresetHls400kId;

    @Value("${aws.system.preset.hls.600k.id}")
    private String awsSystemPresetHls600kId;

    @Value("${aws.system.preset.hls.1m.id}")
    private String awsSystemPresetHls1mId;

    @Value("${aws.system.preset.hls.2m.id}")
    private String awsSystemPresetHls2mId;

    @Value("${aws.encoding.segment.duration.seconds}")
    private String awsEncodingSegmentDurationSeconds;

    @Value("${aws.cloudfront.distribution.url}")
    private String awsCloudFrontDistributionUrl;

    /**
     * Generates a temporary upload URL for a file in an S3 bucket. After the upload the file will be private.
     *
     * @param fileName
     * @param s3Bucket
     * @param expirationDate
     * @return
     */
    public String generatePrivateObjectUploadUrl(String fileName, String s3Bucket, Date expirationDate) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(s3Bucket, fileName);
        generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
        generatePresignedUrlRequest.setExpiration(expirationDate);

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    /**
     * Generates a temporary upload URL for a file in an S3 bucket. After the upload the file can be accessed without authentication
     *
     * @param fileName
     * @param s3Bucket
     * @param expirationDate
     * @return
     */
    public String generatePublicObjectUploadUrl(String fileName, String s3Bucket, Date expirationDate) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(s3Bucket, fileName);
        generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
        generatePresignedUrlRequest.setExpiration(expirationDate);

        // setting public read permissions on the file
        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    /**
     * Generates a temporary download URL for a private file in an S3 bucket
     *
     * @param s3Bucket
     * @param fileName
     * @param expirationDate
     * @return
     */
    public String generatePrivateFileDownloadUrl(String s3Bucket, String fileName, Date expirationDate) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(s3Bucket, fileName);
        generatePresignedUrlRequest.setExpiration(expirationDate);
        generatePresignedUrlRequest.setResponseHeaders(new ResponseHeaderOverrides().withContentDisposition(" attachment; filename=video.mp4"));
        // not setting GET as the HTTP method to be used in the request because it is the default value

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    /**
     * Returns the permanent download URL of a file stored in an S3 bucket
     *
     * @param s3Bucket
     * @param fileName
     * @return
     */
    public String getPublicFileDownloadUrl(String s3Bucket, String fileName) {
        return awsS3Url + s3Bucket + "/" + fileName;
    }

    /**
     * Returns true the file exists in the S3 bucket
     *
     * @param fileName
     * @param s3Bucket
     * @return
     */
    public boolean existsFileInS3Bucket(String fileName, String s3Bucket) {
        return amazonS3Client.doesObjectExist(s3Bucket, fileName);
    }

    /**
     * Runs an AWS Elastic Transcoder job that encodes a video file creating HLS files for it
     *
     * @param s3Bucket      the bucket of the video file that will be encoded
     * @param videoFileName the name of the video file that will be encoded
     * @return the streaming URL of the encoded video file
     */
    public String encodeVideoToHls(String s3Bucket, String videoFileName) {
        // using the video file as the input for the job
        JobInput encodingJobInput = new JobInput().withKey(videoFileName);

        // setup the encoding job outputs using the HLS presets
//		CreateJobOutput hls400kJobOutput = new CreateJobOutput().withKey("hls400k/hls400k").withPresetId(awsSystemPresetHls400kId)
//				.withSegmentDuration(awsEncodingSegmentDurationSeconds);
//        CreateJobOutput hls600kJobOutput = new CreateJobOutput().withKey("hls600k/hls600k").withPresetId(awsSystemPresetHls600kId)
//                .withSegmentDuration(awsEncodingSegmentDurationSeconds);
		CreateJobOutput hls1mJobOutput = new CreateJobOutput().withKey("hls2m/hls2m").withPresetId(awsSystemPresetHls2mId)
				.withSegmentDuration(awsEncodingSegmentDurationSeconds);
//		List<CreateJobOutput> encodingJobOutputs = Arrays.asList(hls400kJobOutput, hls600kJobOutput, hls1mJobOutput);
//        List<CreateJobOutput> encodingJobOutputs = Arrays.asList(hls600kJobOutput);
        List<CreateJobOutput> encodingJobOutputs = Arrays.asList(hls1mJobOutput);

        // setup the master playlist which can be used to play encoded video using adaptive bitrate streaming
//		CreateJobPlaylist playlist = new CreateJobPlaylist().withName("index").withFormat("HLSv3")
//				.withOutputKeys(hls400kJobOutput.getKey(), hls600kJobOutput.getKey(), hls1mJobOutput.getKey());
        CreateJobPlaylist playlist = new CreateJobPlaylist().withName("index").withFormat("HLSv3")
//                .withOutputKeys(hls600kJobOutput.getKey());
                .withOutputKeys(hls1mJobOutput.getKey());

        // Create the job.
        CreateJobRequest createJobRequest = new CreateJobRequest().withPipelineId(awsEncodingPipelineId).withInput(encodingJobInput)
                .withOutputKeyPrefix(videoFileName + "/").withOutputs(encodingJobOutputs).withPlaylists(playlist);
        Job encodingJob = amazonElasticTranscoderClient.createJob(createJobRequest).getJob();

        // waiting for the encoding job to finish
        waitForJobToComplete(encodingJob.getId());

        return generateStreamingURL(videoFileName);
    }

    /**
     * Generates the streaming URL based on the name of the folder of the generated streaming files
     *
     * @param streamingFolder the name of the folder that stores the streaming files
     * @return
     */
    private String generateStreamingURL(String streamingFolder) {
        return "http://" + awsCloudFrontDistributionUrl + "/" + streamingFolder + "/hls2m/hls2m.m3u8";
    }

    /**
     * Waits for the encoding job to complete
     *
     * @param jobId the id of the encoding job
     */
    private void waitForJobToComplete(String jobId) {
        // create a handler that will wait for this encoding job to complete
        JobStatusNotificationHandler notificationHandler = new JobStatusNotificationHandler(jobId);

        // adding the notification handler to the list of notification handlers that are informed every time a message arrives in the AWS SQS
        sqsQueueNotificationWorker.addHandler(notificationHandler);

        synchronized (notificationHandler) {
            try {
                notificationHandler.wait();
            } catch (InterruptedException e) {
                LOGGER.error("Exception occured while waiting blocking the thread by waiting for an AWS encoding job to finish");
                throw new RuntimeException(e);
            }
        }

        // removing the notification handler to the list of notification handlers that are informed every time a message arrives in the AWS SQS
        sqsQueueNotificationWorker.removeHandler(notificationHandler);

        if (notificationHandler.getJobStatusNotificationMessage().getState().equals(JobStatusNotification.JobState.ERROR)) {
            // if an error occurred while encoding the video file
            throw new RuntimeException(String.format("Error occurred while running the encoding job %s, error code: %d", jobId, notificationHandler.getJobStatusNotificationMessage().getErrorCode()));
        }
    }

    /**
     * Deletes a file from an S3 bucket
     *
     * @param s3Bucket the S3 bucket name
     * @param fileName the file name
     */
    public void deleteFile(String s3Bucket, String fileName) {
        amazonS3Client.deleteObject(s3Bucket, fileName);

    }

    /**
     * Deletes all files that begin with the given path
     *
     * @param s3Bucket
     * @param path
     */
    public void deleteFilesWithPath(String s3Bucket, String path) {
        for (S3ObjectSummary file : amazonS3Client.listObjects(s3Bucket, path).getObjectSummaries()) {
            amazonS3Client.deleteObject(s3Bucket, file.getKey());
        }
    }
}
