package binar.box.rest;

import binar.box.dto.PanelDTO;
import binar.box.service.PanelService;
import binar.box.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
@RestController
@RequestMapping(value = Constants.API)
public class PanelController {

    @Autowired
    private PanelService panelService;

    @GetMapping(value = Constants.PANEL_ENDPOINT + Constants.RANDOM_PANEL)
    private List<PanelDTO> getPanels() {
        return panelService.getRandomPanels();
    }


    @GetMapping(value = Constants.PANEL_ENDPOINT)
    private PanelDTO getPanel(@RequestParam("id") long id) {
        return panelService.getPanelDTO(id);
    }
}
