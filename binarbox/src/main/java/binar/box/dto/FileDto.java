package binar.box.dto;

import binar.box.domain.File;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public class FileDto {

    private long id;

    private String name;

    public FileDto() {
    }

    public FileDto(File file) {
        this.id = file.getId();
        this.name = file.getFileName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
