package binar.box.dto;

import java.util.List;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
public class PanelDTO {

    private long id;

    private List<LockResponseDTO> lockResponseDTO;

    public PanelDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<LockResponseDTO> getLockResponseDTO() {
        return lockResponseDTO;
    }

    public void setLockResponseDTO(List<LockResponseDTO> lockResponseDTO) {
        this.lockResponseDTO = lockResponseDTO;
    }
}
