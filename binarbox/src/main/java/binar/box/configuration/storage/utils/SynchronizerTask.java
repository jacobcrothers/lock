package binar.box.configuration.storage.utils;
import binar.box.configuration.storage.AWSVideoService;
import binar.box.domain.Video;
import binar.box.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class SynchronizerTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizerTask.class);

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private AWSVideoService awsService;

    @Value("${aws.video.bucket}")
    private String awsVideosBucket;

    @Value("${aws.hls.bucket}")
    private String awsHlsBucket;

    @Value("${aws.thumbnail.bucket}")
    private String awsThumbnailsBucket;

    /**
     * Checks every 10 minutes that the MySQL Video collection is synchronised with the files stored in the Amazon S3 buckets
     */
    @Scheduled(fixedDelay = 600000)
    public void synchroniseDatabaseWithStorage() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Checking that the MySQL Video is syncronized with the files stored in the Amazon S3 buckets");
        }

        synchroniseVideoResources();
    }

    /**
     * Synchronise the VideoResources collection with the files stored in the Amazon S3 buckets
     */
    private void synchroniseVideoResources() {
        // finding all video resources that have the upload URL expired by at least 10 minutes
        List<Video> videos = videoRepository.findByUploadExpirationDateLessThanAndUploadedSourceFileFalse(Date.from(Instant.now()
                .minusSeconds(600)));

        for (Video video : videos) {

            // deleting the video source file if it exists
            awsService.deleteFile(awsVideosBucket, String.valueOf(video.getId()));

            // deleting the thumbnail file if it exists
            awsService.deleteFile(awsThumbnailsBucket, String.valueOf(video.getId()));

            // deleting the streaming files of the video resource if they exist
            awsService.deleteFilesWithPath(awsHlsBucket, String.valueOf(video.getId()));

            videoRepository.delete(video);
        }

    }
}
