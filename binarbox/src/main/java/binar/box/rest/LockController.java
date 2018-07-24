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
import binar.box.dto.PasswordDTO;
import binar.box.service.FileService;
import binar.box.service.LockService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
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

	@ApiOperation(value = "ADMIN: Add lock type", notes = "This endpoint is for admin, admin add lock types into database.", response = LockTypeDtoResponse.class)
	@PostMapping(value = Constants.LOCK_ENDPOINT
			+ Constants.LOCK_TYPE_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
	private LockTypeDtoResponse addLockType(@RequestBody @Valid LockTypeDTO lockTypeDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new LockBridgesException(bindingResult.getAllErrors().toString());
		}
		return lockService.addLockType(lockTypeDTO);
	}

	@ApiOperation(value = "ADMIN: Add lock images", notes = "This endpoint is for admin, admin add lock images.", response = HttpStatus.class)
	@PostMapping(value = Constants.LOCK_ENDPOINT
			+ Constants.LOCK_TYPE_FILE_ENDPOINT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private void addLockTypeFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("id") long lockTypeId) {
		fileService.saveFilesToLockType(files, lockTypeId);
	}

	@GetMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_TYPE_ENDPOINT)
	private List<LockTypeDtoResponse> getLockTypes() {
		return lockService.getLockTypes();
	}

	@GetMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_SECTION_ENDPOINT)
	private List<LockSection> lockSections() {
		return lockService.getLockSections();
	}

	@PostMapping(value = Constants.LOCK_ENDPOINT)
	private void addLock(@RequestBody LockDTO lockDTO) {
		lockService.addUserLock(lockDTO);
	}

	@PutMapping(value = Constants.LOCK_ENDPOINT)
	private void updateLock(@RequestBody LockDTO lockDTO, @RequestParam("lockId") long lockId) {
		lockService.updateUserLock(lockDTO, lockId);
	}

	@GetMapping(value = Constants.LOCK_ENDPOINT)
	private List<LockResponseDTO> getLocks() {
		return lockService.getLocks();
	}

	@DeleteMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_DELETE_USING_PASSWORD)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	private void removeALockUsingPassword(@RequestParam("id") long id, @RequestBody @Valid PasswordDTO passwordDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new LockBridgesException(bindingResult.getAllErrors().toString());
		}
		lockService.removeUserLock(id, passwordDTO);
	}

	@PostMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_DELETE_USING_TOKEN)
	private void claimToRemoveLock(@RequestParam("id") long id) {
		lockService.claimToRemoveUserLock(id);
	}

	@DeleteMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_DELETE_USING_TOKEN)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	private void removeALockUsingToken(@RequestParam("token") String token) {
		lockService.removeUserLock(token);
	}

}
