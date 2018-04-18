package binar.box.service;

import binar.box.domain.File;
import binar.box.domain.LockType;
import binar.box.dto.FileDto;
import binar.box.dto.LockTypeDto;
import binar.box.dto.LockTypeDtoResponse;
import binar.box.repository.FileRepository;
import binar.box.repository.LockTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<LockTypeDtoResponse> getLockTypes() {
        List<LockType> lockTypes = lockTypeRepository.findAll();
        return lockTypes.stream().map(this::toLockTypeDtoResponse).collect(Collectors.toList());
    }

    private LockTypeDtoResponse toLockTypeDtoResponse(LockType lockType) {
        LockTypeDtoResponse lockTypeDtoResponse = new LockTypeDtoResponse();
        lockTypeDtoResponse.setId(lockType.getId());
        lockTypeDtoResponse.setPrice(lockType.getPrice());
        lockTypeDtoResponse.setType(lockType.getType());
        lockTypeDtoResponse.setFileDtoList(lockType.getFiles().stream().map(this::toFileDto).collect(Collectors.toList()));
        return lockTypeDtoResponse;
    }

    private FileDto toFileDto(File file) {
        return new FileDto(file);
    }
}
