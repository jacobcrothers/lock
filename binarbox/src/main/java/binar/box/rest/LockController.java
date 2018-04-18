package binar.box.rest;

import binar.box.dto.LockTypeDto;
import binar.box.dto.LockTypeDtoResponse;
import binar.box.service.FileService;
import binar.box.service.LockService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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


    @PostMapping(value = Constants.LOCK_TYPE_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
    private LockTypeDtoResponse addLockType(@RequestBody @Valid LockTypeDto lockTypeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new LockBridgesException(bindingResult.getAllErrors().toString());
        }
        return lockService.addLockType(lockTypeDto);
    }

    @PostMapping(value = Constants.LOCK_TYPE_FILE_ENDPOINT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private void addLockTypeFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("lockType") long lockTypeId) {
        fileService.saveFilesToLockType(files, lockTypeId);
    }


}
