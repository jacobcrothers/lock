package binar.box.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;

import binar.box.domain.File;
import binar.box.dto.FileDTO;
import binar.box.dto.PanelDTO;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import binar.box.service.FileService;
import binar.box.util.Constants;
import binar.box.util.Exceptions.LockBridgesException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(Constants.API)
public class FileController {

	private final FileService fileService;

	@Autowired
	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "ADMIN: Add lock category image", notes = "This endpoint is for admin, admin add lock image.", hidden = true)
	@PostMapping(value = Constants.LOCK_ENDPOINT
			+ Constants.LOCK_CATEGORY_FILE_ENDPOINT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity addLockCategoryFile(@RequestParam("file") MultipartFile file, @RequestParam("id") long lockCategoryId) throws IOException {
		fileService.saveFileToLockCategory(file, lockCategoryId);
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "ADMIN: Add bridge image", notes = "This endpoint is for admin, admin add lock images.", hidden = true)
	@PostMapping(value = Constants.LOCK_ENDPOINT
			+ Constants.BRIDGE_ENDPOINT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity addBridgeFile(@RequestParam("file") MultipartFile file) throws IOException {
		fileService.saveBridgeFile(file);
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "ADMIN: Add lock template images", notes = "This endpoint is for admin, admin add lock template images.", hidden = true)
	@PostMapping(value = Constants.LOCK_ENDPOINT
			+ Constants.LOCK_TEMPLATE_FILE_ENDPOINT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity addLockTemplateFiles(@RequestParam("file") MultipartFile[] files,
												@RequestParam("id") long lockTemplateId,
												@RequestParam("type") File.Type type) throws IOException {
		fileService.saveFilesToLockTemplate(files, lockTemplateId, type);
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Send lock bridge video to user timeline", notes = "Facebook video upload", hidden = true)
	@PostMapping(value = Constants.LOCK_ENDPOINT
			+ Constants.BRIDGE_VIDEO_ENDPOINT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity uploadVideo(@RequestParam("file") MultipartFile video) throws IOException {
		return new ResponseEntity<>(fileService.uploadVideoWithFbRest(video), HttpStatus.CREATED);
	}


	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get bridge with all locks", notes = "This endpoint is used for \"see all locks\" feature, here are displayed public panels with public locks.")
	@GetMapping(value = Constants.BRIDGE_LOCKS)
	@ResponseStatus(HttpStatus.OK)
	private void getBridgePictureWithLocks() throws IOException {
		fileService.downloadBridgeFile();
	}

	@RequestMapping(value = "/download/file/{fileId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void downloadFile(@PathVariable(name = "fileId") Long fileId,
							 HttpServletResponse response) throws IOException {
		HttpHeaders responseHeaders = new HttpHeaders();
		try (InputStream productPicFIS = fileService.downloadFile(fileId)) {
			FileDTO fileDTO = fileService.getFile(fileId);
			String mimeType = URLConnection.guessContentTypeFromName(fileDTO.getName());
			responseHeaders.setContentType(MediaType.valueOf(mimeType));
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "file"));
			IOUtils.copy(productPicFIS, response.getOutputStream());
		} catch (Exception e) {
			responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		}
	}
}
