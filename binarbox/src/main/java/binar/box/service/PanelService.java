package binar.box.service;

import binar.box.domain.Lock;
import binar.box.domain.Panel;
import binar.box.dto.LockResponseDTO;
import binar.box.dto.PanelDTO;
import binar.box.repository.PanelRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
@Service
public class PanelService {

    @Autowired
    private PanelRepository panelRepository;

    @Autowired
    private LockService lockService;

    public List<PanelDTO> getRandomPanels() {
        return panelRepository.findRandomPanels().parallelStream().map(this::toPanelDto).collect(Collectors.toList());
    }

    private PanelDTO toPanelDto(Panel panel) {
        var panelDTO = new PanelDTO();
        panelDTO.setId(panel.getId());
        var lockResponseDTOList = panel.getLocks().parallelStream().map(this::toLockResponse).collect(Collectors.toList());
        panelDTO.setLockResponseDTO(lockResponseDTOList);
        return panelDTO;
    }

    private LockResponseDTO toLockResponse(Lock lock) {
        return lockService.toLockResponseDto(lock);
    }

    Panel getPanel(long panelId) {
        return panelRepository.findById(panelId).orElseThrow(() -> new LockBridgesException(Constants.PANEL_NOT_FOUND));
    }

    public PanelDTO getPanelDTO(long id) {
        return toPanelDto(getPanel(id));
    }
}
