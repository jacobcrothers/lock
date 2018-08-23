package binar.box.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import binar.box.service.FileService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
@RestController
@RequestMapping(Constants.API + Constants.FILE_ENDPOINT)
public class FileController {

	@Autowired
	private FileService fileService;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Download file", notes = "File get", hidden = true)
	@GetMapping(value = Constants.DOWNLOAD, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	private ResponseEntity<InputStreamResource> downloadFile(@RequestParam("id") long fileId) {
		binar.box.domain.File fileEntity = fileService.getFile(fileId);
		File file = new File(fileEntity.getPathToFile());
		InputStreamResource inputStreamResource;
		try {
			inputStreamResource = new InputStreamResource(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new LockBridgesException(Constants.EXCEPTION_DOWNLOADING_THE_FILE + e.getMessage());
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileEntity.getFileName())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length()).body(inputStreamResource);
	}

}
