package binar.box.service;

import binar.box.configuration.storage.AWSVideoService;
import binar.box.configuration.storage.FileStorage;
import binar.box.configuration.storage.StreamingHelper;
import binar.box.converter.VideoConverter;
import binar.box.domain.Lock;
import binar.box.domain.User;
import binar.box.domain.Video;
import binar.box.dto.VideoDTO;
import binar.box.dto.VideoUploadDTO;
import binar.box.repository.LockRepository;
import binar.box.repository.UserRepository;
import binar.box.repository.VideoRepository;
import binar.box.util.Exceptions.EntityNotFoundException;
import binar.box.util.Exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;


/**
 * Created by Andrei Lazar on 5/18/2017.
 */
@Service
@Transactional
public class VideoService {

    @Autowired
    private LockRepository lockRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoConverter videoConverter;

    @Autowired
    private AWSVideoService awsService;

    @Autowired
    private FileStorage fileStorage;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StreamingHelper streamingHelper;

    @Value("${aws.video.bucket}")
    private String awsVideosBucket;

    @Value("${aws.hls.bucket}")
    private String awsHlsBucket;

    @Value("${aws.thumbnail.bucket}")
    private String awsThumbnailsBucket;

    @Value("${aws.upload.expiration.seconds}")
    private long awsUploadExpirationSeconds;

    @Value("${spring.carehome.file-service.domain}")
    private String domain;


    public VideoUploadDTO getUploadUrl(Long lockId, String videoName) {

        Lock lock = lockRepository.findOne(lockId);

        Video video = new Video();
        Date expirationDate = Date.from(Instant.now().plusSeconds(awsUploadExpirationSeconds));
        video.setUploadExpirationDate(expirationDate);
        videoRepository.save(video);

        String videoUploadUrl = awsService.generatePrivateObjectUploadUrl(String.valueOf(video.getId()), awsVideosBucket, expirationDate);
        String thumbnailUploadURL = awsService.generatePublicObjectUploadUrl(String.valueOf(video.getId()), awsThumbnailsBucket, expirationDate);

        // generating the permanent download URL of the thumbnail
        String thumbnailDownloadURL = awsService.getPublicFileDownloadUrl(awsThumbnailsBucket, String.valueOf(video.getId()));

        video.setThumbnailURL(thumbnailDownloadURL);
        video.setLock(lock);
        video.setName(videoName);

        //second save - for the url that needs the id
        videoRepository.save(video);


        // creating the video resource that represents the source video file that will be uploaded to the Amazon S3 bucket
        VideoUploadDTO result = new VideoUploadDTO();
        result.setExpirationTime(expirationDate);
        result.setVideoId(video.getId());
        result.setVideoUploadURL(videoUploadUrl);
        result.setThumbnailURL(thumbnailUploadURL);
        return result;
    }

    public VideoDTO confirmVideoSourceFileUpload(long videoId) {
        Video video = videoRepository.findOne(videoId);
        if (video == null) {
            throw new EntityNotFoundException("Unavailable video resource");
        }

        if (!awsService.existsFileInS3Bucket(String.valueOf(video.getId()), awsVideosBucket)) {
            throw new FileStorageException("The video source file was not uploaded");
        }
        if (!awsService.existsFileInS3Bucket(String.valueOf(video.getId()), awsThumbnailsBucket)) {
            throw new FileStorageException("The thumbnail image was not uploaded");
        }

        String streamingUrl = awsService.encodeVideoToHls(awsVideosBucket, String.valueOf(video.getId()));
        video.setStreamingURL(streamingUrl);
        video.setUploadedSourceFile(true);
        video.setUploadTime(new Date());

        return videoConverter.toDTO(video);
    }
}
