package binar.box.configuration.storage;

import binar.box.domain.Video;
import binar.box.repository.VideoRepository;
import binar.box.util.Exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StreamingHelper {
    @Autowired
    private AWSVideoService awsService;

    @Autowired
    private VideoRepository videoRepository;

    @Value("${aws.video.bucket}")
    private String awsVideosBucket;

    @Value("${aws.hls.bucket}")
    private String awsHlsBucket;

    @Value("${aws.thumbnail.bucket}")
    private String awsThumbnailsBucket;

    public void deleteStreamingAsset(Long streamingAssetId) {
        Video video = videoRepository.findByIdAndUploadedSourceFileTrue(streamingAssetId);
        if (video == null) {
            throw new FileStorageException("Unavailable streaming resource");
        }
        // deleting the video source file
        awsService.deleteFile(awsVideosBucket, String.valueOf(video.getId()));

        // deleting the thumbnail file
        awsService.deleteFile(awsThumbnailsBucket, String.valueOf(video.getId()));

        // deleting the streaming files of the video resource
        awsService.deleteFilesWithPath(awsHlsBucket, String.valueOf(video.getId()));

    }
}
