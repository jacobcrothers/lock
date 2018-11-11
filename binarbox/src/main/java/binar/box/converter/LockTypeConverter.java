package binar.box.converter;

import binar.box.domain.LockCategory;
import binar.box.dto.LockCategoryDTOResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class LockTypeConverter {

    @Autowired
    private FileConverter fileConverter;

    @Autowired
    private LockTypeTemplateConverter lockTypeTemplateConverter;

    public LockCategoryDTOResponse lockToLockTypeResponse(LockCategory lockCategory){
        return LockCategoryDTOResponse.builder()
                .id(lockCategory.getId())
                .price(lockCategory.getPrice().getPrice())
                .category(lockCategory.getCategory())
                .lockTypeTemplate(Objects.isNull(lockCategory.getLockTypeTemplate()) ? null :
                        lockTypeTemplateConverter.toDTOList(lockCategory.getLockTypeTemplate()))
                .filesDTO(Objects.isNull((lockCategory.getFiles())) ? null :
                        fileConverter.toDTOList(lockCategory.getFiles()))
                .build();
    }

    public List<LockCategoryDTOResponse> toDTOList(List<LockCategory> lockCategories){
        return lockCategories.stream().map(this::lockToLockTypeResponse).collect(Collectors.toList());
    }
}
