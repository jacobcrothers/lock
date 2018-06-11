package binar.box.dto;

import binar.box.domain.LockType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public class LockTypeDtoResponse {

    private long id;

    private String type;

    private BigDecimal price;

    private List<FileDTO> fileDTOList;

    public LockTypeDtoResponse() {
    }

    public LockTypeDtoResponse(LockType lockType) {
        this.id = lockType.getId();
        this.type = lockType.getType();
        this.price = lockType.getPrice();
    }

    public List<FileDTO> getFileDtoList() {
        return fileDTOList;
    }

    public void setFileDtoList(List<FileDTO> fileDTOList) {
        this.fileDTOList = fileDTOList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
