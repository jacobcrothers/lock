package binar.box.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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
@Transactional
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
//		var user = userService.getAuthenticatedUser();
//		
		var panels = panelRepository.findAll();
//		var panels = panelRepository.findAllPanelsBasedOnLocation(user.getCountry(),
//				configurationRepository.findById(Long.valueOf(1)).get().getMaxPanelsView());
//		
//		panels = panels.parallelStream().map(this::insertRandomLocks).collect(Collectors.toList());
		panels = panels.parallelStream().map(this::addPanelLocks).collect(Collectors.toList());
//		var maxLights = configurationRepository.findById(Long.valueOf(1)).get().getGlitteringLightsOnLocks();
//		var maxPanels = panels.size();
//		addLockLights(panels, maxLights, maxPanels);
		return panels.parallelStream().map(this::toPanelDTO).collect(Collectors.toList());
	}

	private int randomIntegerUsingMath(int min, int max) {
		return (int) (Math.random() * (max - min)) + min;
	}

	private int randomIntegerUsingRandom(int min, int max) {
		Random random = new Random();
		return random.nextInt(max + 1 - min) + min;
	}

	private void addLockLights(List<Panel> panels, int maxLights, int maxPanels) {
		var ligtsOnLocks = 0;
		var initialMaxValue = maxLights;
		var totalLigthsOnLocks = 0;
		for (Panel panel : panels) {
			int lightsPerPanel = randomIntegerUsingMath(0, maxLights);
			var locks = panel.getLocks();
			if (lightsPerPanel == 0) {
				if (maxLights > 0) {
					lightsPerPanel = randomIntegerUsingRandom(0, maxLights);
				}
			}
			ligtsOnLocks = setLockLight(lightsPerPanel, locks);
			maxLights -= ligtsOnLocks;
			if (maxLights == 0) {
				return;
			}
			totalLigthsOnLocks += ligtsOnLocks;
		}
		if (totalLigthsOnLocks < initialMaxValue) {
			addLockLights(panels, initialMaxValue - totalLigthsOnLocks, maxPanels);
		}
	}

	private int setLockLight(int lightsPerPanel, List<Lock> locks) {
		var lightsOnLock = 0;
		var lockIndex = 0;
		for (var index = 1; index <= lightsPerPanel; index++) {
			while (lockIndex < locks.size()) {
				var lock = locks.get(lockIndex);
				lock.setGlitteringLight(true);
				lightsOnLock++;
				lockIndex++;
				break;
			}
		}
		return lightsOnLock;
	}

	private PanelDTO toPanelDTO(Panel panel) {
		var panelDTO = new PanelDTO();
		panelDTO.setId(panel.getId());
		var lockResponseDTOList = panel.getLocks().parallelStream().map(this::toLockResponse)
				.collect(Collectors.toList());
		panelDTO.setLockResponseDTO(lockResponseDTOList);
		return panelDTO;
	}

	private LockResponseDTO toLockResponse(Lock lock) {
		return lockService.toLockResponseDTO(lock);
	}

	Panel getPanel(long panelId) {
		return panelRepository.findById(panelId).orElseThrow(() -> new LockBridgesException(Constants.PANEL_NOT_FOUND));
	}

	public PanelDTO getPanelDTO(long id) {
		return toPanelDTO(getPanel(id));
	}

	public List<PanelDTO> getUserLocksAndPanels() {
		var user = userService.getAuthenticatedUser();
		var panelsOfUser = getPanelsWhereUserHasLocks(user);
		var facebookUserFriends = userService.getUserFacebookFriends(user);
		panelsOfUser = panelsOfUser.parallelStream().map(panel -> addPanelLocks(panel, user, facebookUserFriends))
				.collect(Collectors.toList());
		panelsOfUser = panelsOfUser.parallelStream().map(this::insertRandomLocks).collect(Collectors.toList());
		return panelsOfUser.parallelStream().map(this::toPanelDTO).collect(Collectors.toList());
	}

	private Panel addPanelLocks(Panel panel) {
		if (panel.getLocks() == null) {
			panel.setLocks(lockRepository.findByPanelId(panel.getId()));
		} else {
			panel.getLocks().addAll(lockRepository.findByPanelId(panel.getId()));
		}
		return panel;
	}

	private Panel addPanelLocks(Panel panel, User user, List<String> facebookUserFriends) {
		panel.setLocks(lockRepository.findUserPanelLocksAndHidePrivateFriendsLocks(user.getId(), panel.getId(),
				facebookUserFriends));
		return panel;
	}

	private Panel insertRandomLocks(Panel panel) {
		var panelMaxSize = configurationRepository.findById(Long.valueOf(1)).get().getPanelMaxSizeOfLocks();
		var numberOfRandomLocksOnUserPanel = configurationRepository.findById(Long.valueOf(1)).get()
				.getRandomLocksOnUserPanel();
		List<Lock> locks = panel.getLocks() == null ? new ArrayList<>() : panel.getLocks();
		for (var times = 0; times < numberOfRandomLocksOnUserPanel; times++) {
			if (locks.size() >= panelMaxSize) {
				break;
			}
			var lock = new Lock();
			lock.setId(Long.valueOf(times + 1000));
			locks.add(lock);
			panel.setLocks(locks);
		}
		return panel;
	}

	private List<Panel> getPanelsWhereUserHasLocks(User user) {
		return panelRepository.findByUser(user.getId());
	}
}
