package binar.box.rest;

import binar.box.domain.LockSection;
import binar.box.dto.LockDTO;
import binar.box.dto.LockResponseDTO;
import binar.box.dto.LockTypeDTO;
import binar.box.dto.LockTypeDtoResponse;
import binar.box.service.FileService;
import binar.box.service.LockService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Timis Nicu Alexandru on 16-Apr-18.
 */
@RestController
@RequestMapping(value = Constants.API + Constants.LOCK_ENDPOINT)
public class LockController {

    @Autowired
    private LockService lockService;

    @Autowired
    private FileService fileService;


    @ApiOperation(value = "ADMIN: Add lock type", notes = "This endpoint is for admin, admin add lock types into database.", response = LockTypeDtoResponse.class)
    @PostMapping(value = Constants.LOCK_TYPE_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
    private LockTypeDtoResponse addLockType(@RequestBody @Valid LockTypeDTO lockTypeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new LockBridgesException(bindingResult.getAllErrors().toString());
        }
        return lockService.addLockType(lockTypeDTO);
    }

    @ApiOperation(value = "ADMIN: Add lock images", notes = "This endpoint is for admin, admin add lock images.", response = HttpStatus.class)
    @PostMapping(value = Constants.LOCK_TYPE_FILE_ENDPOINT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private void addLockTypeFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("id") long lockTypeId) {
        fileService.saveFilesToLockType(files, lockTypeId);
    }


    @GetMapping(value = Constants.LOCK_TYPE_ENDPOINT)
    private List<LockTypeDtoResponse> getLockTypes() {
        return lockService.getLockTypes();
    }


    @GetMapping(value = Constants.LOCK_SECTION_ENDPOINT)
    private List<LockSection> lockSections() {
        return lockService.getLockSections();
    }

    @PostMapping(value = Constants.LOCK_ACTION_ENDPOINT)
    private void addLock(@RequestBody @Valid LockDTO lockDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new LockBridgesException(bindingResult.getAllErrors().toString());
        }
        lockService.addUserLock(lockDTO);
    }

    @GetMapping(value = Constants.LOCK_ACTION_ENDPOINT)
    private List<LockResponseDTO> getLocks() {
        return lockService.getLocks();
    }

    @DeleteMapping(value = Constants.LOCK_ACTION_ENDPOINT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void removeALock(@RequestParam("id") long id) {
        lockService.removeUserLock(id);
    }

}
