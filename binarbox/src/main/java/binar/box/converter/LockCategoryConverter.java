package binar.box.converter;

import binar.box.domain.LockCategory;
import binar.box.dto.LockCategoryDTOResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class LockCategoryConverter {

    @Autowired
    private FileConverter fileConverter;

    @Autowired
    private LockTemplateConverter lockTemplateConverter;

    public LockCategoryDTOResponse lockToLockCategoryResponse(LockCategory lockCategory){
        return LockCategoryDTOResponse.builder()
                .id(lockCategory.getId())
                .price(lockCategory.getPrice().getPrice())
                .category(lockCategory.getCategory())
                .lockTypeTemplate(Objects.isNull(lockCategory.getLockTemplate()) ? null :
                        lockTemplateConverter.toDTOList(lockCategory.getLockTemplate()))
                .fileDTO(Objects.isNull((lockCategory.getFile())) ? null :
                        fileConverter.toDTO(lockCategory.getFile()))
                .build();
    }

    public List<LockCategoryDTOResponse> toDTOList(List<LockCategory> lockCategories){
        return lockCategories.stream().map(this::lockToLockCategoryResponse).collect(Collectors.toList());
    }
}
