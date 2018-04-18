package binar.box.service;

import binar.box.domain.LockType;
import binar.box.dto.LockTypeDto;
import binar.box.dto.LockTypeDtoResponse;
import binar.box.repository.FileRepository;
import binar.box.repository.LockTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
@Service
@Transactional
public class LockService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private LockTypeRepository lockTypeRepository;

    @Autowired
    private FileService fileService;


    public LockTypeDtoResponse addLockType(LockTypeDto lockTypeDto) {
        LockType lockType = new LockType();
        lockType.setPrice(lockTypeDto.getPrice());
        lockType.setType(lockTypeDto.getType());
        lockTypeRepository.save(lockType);
        return new LockTypeDtoResponse(lockType);
    }
}
