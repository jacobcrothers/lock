package binar.box.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import binar.box.dto.PanelDTO;
import binar.box.service.PanelService;
import binar.box.util.Constants;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = Constants.API)
public class PanelController {

	private final PanelService panelService;

	@Autowired
	public PanelController(PanelService panelService) {
		this.panelService = panelService;
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get panels", notes = "This endpoint is used for \"see all locks\" feature, here are displayed public panels with public locks.")
	@GetMapping(value = Constants.PANEL_ENDPOINT)
	@ResponseStatus(HttpStatus.OK)
	private List<PanelDTO> getPanels() {
		return panelService.getAllPanels();
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get panel", notes = "Get panels", hidden = true)
	@GetMapping(value = Constants.PANEL_ENDPOINT + Constants.USER)
	@ResponseStatus(HttpStatus.OK)
	private PanelDTO getUserAndUserFriendLocksAndPanel(@RequestParam("panelId") Long panelId) {
		return panelService.getUserLocksAndPanel(panelId);
	}
}
