package binar.box.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import binar.box.domain.LockSection;
import binar.box.dto.LockDTO;
import binar.box.dto.LockResponseDTO;
import binar.box.dto.LockTypeDTO;
import binar.box.dto.LockTypeDtoResponse;
import binar.box.service.FileService;
import binar.box.service.LockService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by Timis Nicu Alexandru on 16-Apr-18.
 */
@RestController
@RequestMapping(value = Constants.API)
public class LockController {

	@Autowired
	private LockService lockService;

	@Autowired
	private FileService fileService;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "ADMIN: Add lock type", notes = "This endpoint is for admin, admin add lock types into database.", hidden = true)
	@PostMapping(value = Constants.LOCK_ENDPOINT
			+ Constants.LOCK_TYPE_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
	private LockTypeDtoResponse addLockType(@RequestBody @Valid LockTypeDTO lockTypeDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new LockBridgesException(bindingResult.getAllErrors().toString());
		}
		return lockService.addLockType(lockTypeDTO);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "ADMIN: Add lock images", notes = "This endpoint is for admin, admin add lock images.", hidden = true)
	@PostMapping(value = Constants.LOCK_ENDPOINT
			+ Constants.LOCK_TYPE_FILE_ENDPOINT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private void addLockTypeFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("id") long lockTypeId) {
		fileService.saveFilesToLockType(files, lockTypeId);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get lock type with lock type templates", notes = "User first step is to choose one lock type then one lock template type")
	@GetMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_TYPE_ENDPOINT)
	private List<LockTypeDtoResponse> getLockTypes() {
		return lockService.getLockTypes();
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@GetMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_SECTION_ENDPOINT)
	private List<LockSection> lockSections() {
		return lockService.getLockSections();
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@PostMapping(value = Constants.LOCK_ENDPOINT)
	private void addLock(@RequestBody LockDTO lockDTO) {
		lockService.addOrUpdateUserLock(lockDTO);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@PutMapping(value = Constants.LOCK_ENDPOINT)
	private void updateLock(@RequestBody LockDTO lockDTO) {
		lockService.addOrUpdateUserLock(lockDTO);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@GetMapping(value = Constants.LOCK_ENDPOINT)
	private List<LockResponseDTO> getLocks() {
		return lockService.getLocks();
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@PostMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_DELETE_USING_TOKEN)
	private void claimToRemoveLock(@RequestParam("id") long id) {
		lockService.claimToRemoveUserLock(id);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@DeleteMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_DELETE_USING_TOKEN)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	private void removeALockUsingToken(@RequestParam("token") String token) {
		lockService.removeUserLock(token);
	}

}
