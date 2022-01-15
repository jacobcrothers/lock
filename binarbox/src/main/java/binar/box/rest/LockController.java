package binar.box.rest;

import binar.box.domain.File;
import binar.box.dto.*;
import binar.box.service.FileService;
import binar.box.service.LockService;
import binar.box.util.Constants;
import binar.box.util.Exceptions.FieldsException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping(value = Constants.API)
public class LockController {

	private final LockService lockService;

	@Autowired
	public LockController(LockService lockService) {
		this.lockService = lockService;
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "ADMIN: Add lock category", notes = "This endpoint is for admin, admin add lock category into database.", hidden = true)
	@PostMapping(value = Constants.LOCK_ENDPOINT
			+ Constants.LOCK_CATEGORY_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<LockCategoryDTOResponse> addLockCategory(@Valid @RequestBody LockCategoryDTO lockCategoryDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new FieldsException("Lock category fields are incorrect", "lock.category.invalid", bindingResult);
		}
		return new ResponseEntity<>(lockService.addLockCategory(lockCategoryDTO), HttpStatus.CREATED);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get lock category with lock templates", notes = "User first step is to choose one lock category then one lock template type")
	@GetMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_CATEGORY_ENDPOINT)
	private ResponseEntity<List<LockCategoryDTOResponse>> getLockCategories() {
		return new ResponseEntity<>(lockService.getLockCategories(), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get lock sections", notes = "This endpoint response reveal available sections of panels.")
	@GetMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_SECTION_ENDPOINT)
	private ResponseEntity<List<LockSectionDTO>> lockSections() {
		return new ResponseEntity<>(lockService.getLockSections(), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Add user lock", notes = "Mandatory fields: lockTemplate,lockCategory." + "\n EX: {\r\n"
			+ "  \"fontColor\": \"BLUE\",\r\n" + "  \"fontSize\": 60,\r\n" + "  \"fontStyle\": \"ROBOTO\",\r\n"
			+ "  \"message\":\"MEsSAGE BECAUSE I CAN \",\r\n" + "  \"lockCategory\":3,\r\n" + "  \"lockTemplate\":7\r\n"
			+ "} " + "\n This is the second user step to add a lock.")
	@PostMapping(value = Constants.LOCK_ENDPOINT + "Obsolete")
	@Deprecated
	private ResponseEntity<LockResponseDTO> addLock(@RequestBody LockDTO lockDTO) throws IOException {
			return new ResponseEntity<>(lockService.createUserLock(lockDTO), HttpStatus.CREATED);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "1st step in adding user lock", notes = "Mandatory fields: lockTemplate, message, private")
	@PostMapping(value = Constants.LOCK_ENDPOINT)
	private ResponseEntity<LockStepOneDTO> addLockStepOne(@Valid @RequestBody LockStepOneDTO lockStepOneDTO, BindingResult bindingResult) throws IOException {
		if (bindingResult.hasErrors()) {
			throw new FieldsException("Lock step one fields are incorrect", "lock.step.one.invalid", bindingResult);
		}
		return new ResponseEntity<>(lockService.createUserLock(lockStepOneDTO), HttpStatus.CREATED);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@PutMapping(value = Constants.LOCK_ENDPOINT)
	@ApiOperation(value = "Update user lock", notes = "Mandatory fields: lockTemplate,lockCategory." + "\n {\r\n"
			+ "  \"id\":1,\r\n" + "  \"fontColor\": \"string\",\r\n" + "  \"fontStyle\":\"ROBOTO\",\r\n"
			+ "  \"lockTemplate\":6,\r\n" + "  \"lockCategory\": 3,\r\n" + "  \"longitude\": 0,\r\n"
			+ "  \"message\": \"string\",\r\n" + "  \"lockColor\":\"YELLOW\",\r\n" + "  \"lockSection\":1\r\n" + "\r\n"
			+ "}" + "\n This is the third user step to add a lock")
	@Deprecated
	private ResponseEntity<LockResponseDTO> updateLock(@RequestBody LockDTO lockDTO) {
		return new ResponseEntity<>(lockService.updateUserLock(lockDTO), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@PutMapping(value = Constants.UPDATE_LOCK_SECTION_ENDPOINT)
	@ApiOperation(value = "Update user lock section", notes = "Mandatory fields: lockId, sectionId")
	private ResponseEntity<LockResponseDTO> updateLockSection(@PathVariable("lockId") long lockId,
															  @PathVariable("sectionId") long sectionId) {
		return new ResponseEntity<>(lockService.updateUserLockSection(lockId, sectionId), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@PutMapping(value = Constants.UPDATE_LOCK_PAID_ENDPOINT)
	@ApiOperation(value = "Update user lock paid flag", notes = "Mandatory fields: lockId")
	private ResponseEntity<LockResponseDTO> updateLockPaid(@PathVariable("lockId") long lockId) {
		return new ResponseEntity<>(lockService.updateUserLockPaid(lockId), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get unpaid/in progress user locks", notes = "For pending locks tab")
	@GetMapping(value = Constants.LOCK_UNPAID_ENDPOINT)
	private ResponseEntity<List<LockResponseDTO>> getUnpaidLocks() {
		return new ResponseEntity<>(lockService.getUnpaidLocks(), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Claim to remove user lock", notes = "Requesting this endpoint will send a token on user email which can be used to remove a lock.", hidden = true)
	@PostMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_DELETE_USING_TOKEN)
	private ResponseEntity claimToRemoveLock(@RequestParam("id") long id) {
		lockService.claimToRemoveUserLock(id);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Delete user lock", notes = "Requesting this endpoint will delete user lock using the token for more security.", hidden = true)
	@DeleteMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_DELETE_USING_TOKEN)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	private void removeALockUsingToken(@RequestParam("token") String token) {
		lockService.removeUserLock(token);
	}

}
