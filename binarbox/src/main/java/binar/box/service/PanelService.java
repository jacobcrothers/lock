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
import binar.box.repository.ConfigurationRepository;
import binar.box.repository.LockRepository;
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
	private LockRepository lockRepository;
	@Autowired
	private PanelRepository panelRepository;
	@Autowired
	private LockService lockService;
	@Autowired
	private UserService userService;
	@Autowired
	private ConfigurationRepository configurationRepository;

	public List<PanelDTO> getAllPanels() {
		var user = userService.getAuthenticatedUser();
		return panelRepository.findAllPanelsBasedOnLocation(user.getCountry()).parallelStream().map(this::toPanelDto)
				.collect(Collectors.toList());
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
		if (user.isLinkedWithFacebbok()) {
			var facebookUserFriends = userService.getUserFacebookFriends(user);
			panelsOfUser = insertPanelLocks(user, panelsOfUser, facebookUserFriends);

		} else {
			panelsOfUser = insertPanelLocks(panelsOfUser);
		}
		panelsOfUser = insertRandomLocks(panelsOfUser);
		return panelsOfUser.parallelStream().map(this::toPanelDto).collect(Collectors.toList());
	}

	private List<Panel> insertPanelLocks(List<Panel> panelsOfUser) {
		return panelsOfUser.parallelStream().map(panel -> addPanelLocks(panel)).collect(Collectors.toList());
	}

	private Panel addPanelLocks(Panel panel) {
		panel.setLocks(lockRepository.findByPanelId(panel.getId()));
		return panel;
	}

	private List<Panel> insertPanelLocks(User user, List<Panel> panelsOfUser, List<String> facebookUserFriends) {
		return panelsOfUser.parallelStream().map(panel -> addPanelLocks(panel, user, facebookUserFriends))
				.collect(Collectors.toList());
	}

	private Panel addPanelLocks(Panel panel, User user, List<String> facebookUserFriends) {
		panel.setLocks(lockRepository.findUserPanelLocksAndHidePrivateFriendsLocks(user.getId(), panel.getId(),
				facebookUserFriends));
		return panel;
	}

	private List<Panel> insertRandomLocks(List<Panel> panelsOfUser) {
		var panelMaxSize = configurationRepository.findById(Long.valueOf(1)).get().getPanelMaxSize();
		var numberOfRandomLocksOnUserPanel = configurationRepository.findById(Long.valueOf(1)).get()
				.getRandomLocksOnUserPanel();
		for (Panel panel : panelsOfUser) {
			var locks = panel.getLocks();
			for (var times = 0; times < numberOfRandomLocksOnUserPanel; times++) {
				if (locks.size() >= panelMaxSize) {
					break;
				}
				var lock = new Lock();
				lock.setId(Long.valueOf(times + 1000));
				locks.add(lock);
			}
			panel.setLocks(locks);
		}
		return panelsOfUser;
	}

	private List<Panel> getPanelsWhereUserHasLocks(User user) {
		return panelRepository.findByUser(user.getId());
	}
}
