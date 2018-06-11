package binar.box.rest;

import binar.box.domain.Panel;
import binar.box.repository.PanelRepository;
import binar.box.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
@RestController
@RequestMapping(value = Constants.API)
public class PanelController {

    @Autowired
    private PanelRepository panelRepository;

    @GetMapping(value = Constants.PANEL_ENDPOINT)
    private List<Panel> getPanels() {
        return panelRepository.findPanel();
    }


}
