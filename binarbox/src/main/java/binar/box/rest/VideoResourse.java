package binar.box.rest;

import binar.box.configuration.storage.StreamingHelper;
import binar.box.dto.VideoDTO;
import binar.box.dto.VideoUploadDTO;
import binar.box.service.VideoService;
import binar.box.util.Constants;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Video API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = Constants.API + "/video")
public class VideoResourse {

    @Autowired
    private VideoService videoService;

    @Autowired
    private StreamingHelper streamingHelper;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header")})
    @ApiOperation(value = "Get a temporarly PUT URL for uploading a video file and a temporarly PUT URL for uploading a thumbnail image for that video file", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, code = 201)
    @RequestMapping(value = "/temporaryUploadUrl/lock/{lockId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoUploadDTO> getTemporaryUploadURL(@PathVariable Long lockId,
                                                                @RequestParam(name = "videoName", required = true) String videoName) {
        return new ResponseEntity<>(videoService.getUploadUrl(lockId, videoName), HttpStatus.OK);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header")})
    @ApiOperation(value = "Confirm that a video file was uploaded to Amazon S3 and returns the streaming URL of the video")
    @RequestMapping(value = "/confirmFileUpload/{videoId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoDTO> confirmFileUpload(@PathVariable Long videoId) {
        return new ResponseEntity<>(videoService.confirmVideoSourceFileUpload(videoId), HttpStatus.OK);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header")})
    @ApiOperation(value = "Get lock video")
    @RequestMapping(value = "/lock/{lockId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoDTO> getVideoLock(@PathVariable Long lockId) {
        return new ResponseEntity<>(videoService.getVideoForLock(lockId), HttpStatus.OK);
    }

}
