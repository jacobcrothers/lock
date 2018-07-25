package binar.box.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import binar.box.domain.Lock;
import binar.box.domain.Panel;
import binar.box.domain.User;
import binar.box.dto.LockResponseDTO;
import binar.box.dto.PanelDTO;
import binar.box.repository.PanelRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
@Service
public class PanelService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PanelRepository panelRepository;
	@Autowired
	private LockService lockService;
	@Autowired
	private UserService userService;

	public List<PanelDTO> getRandomPanels() {
		return panelRepository.findRandomPanels().parallelStream().map(this::toPanelDto).collect(Collectors.toList());
	}

	private PanelDTO toPanelDto(Panel panel) {
		var panelDTO = new PanelDTO();
		panelDTO.setId(panel.getId());
		var lockResponseDTOList = panel.getLocks().parallelStream().map(this::toLockResponse)
				.collect(Collectors.toList());
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

	void maintainPanels() {
		log.info(Constants.MAINTAINING_PANELS);
		var numberOfPanelsAvailable = panelRepository.countPanels()
				.orElseThrow(() -> new LockBridgesException(Constants.SOMETHING_WENT_WRONG_WITH_PANELS_COUNTING));
		if (numberOfPanelsAvailable.intValue() < 2) {
			panelRepository.addPanels(2);
		} else if (numberOfPanelsAvailable.intValue() <= 2) {
			panelRepository.addPanels(1);
		}
	}

	public List<PanelDTO> getUserLocksAndPanels() {
		var user = userService.getAuthenticatedUser();
		var panelsOfUser = getPanelsWhereUserHasLocks(user);
		return panelsOfUser.parallelStream().map(this::toPanelDto).collect(Collectors.toList());
	}

	private List<Panel> getPanelsWhereUserHasLocks(User user) {
		return panelRepository.findByUser(user.getId());
	}
}
