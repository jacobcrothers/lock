package binar.box.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import binar.box.domain.Lock;
import binar.box.domain.LockSection;
import binar.box.domain.Panel;
import binar.box.domain.User;
import binar.box.dto.LockResponseDTO;
import binar.box.dto.LockSectionDTO;
import binar.box.dto.PanelDTO;
import binar.box.repository.ConfigurationRepository;
import binar.box.repository.LockRepository;
import binar.box.repository.LockSectionRepository;
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
	@Autowired
	private LockSectionRepository lockSectionRepository;

	public List<PanelDTO> getAllPanels() {
		var panels = panelRepository.findAll();
		panels = panels.parallelStream().map(this::addPanelLocks).collect(Collectors.toList());
		return panels.parallelStream().map(this::toPanelDTO).collect(Collectors.toList());
	}

	private PanelDTO toPanelDTO(Panel panel) {
		var panelDTO = new PanelDTO();
		panelDTO.setId(panel.getId());
		panelDTO.setLockSectionDTO(
				panel.getLockSection().parallelStream().filter(lockSection -> lockSection.getLock() != null)
						.map(this::toLockSectionDTO).collect(Collectors.toList()));
		return panelDTO;
	}

	private LockSectionDTO toLockSectionDTO(LockSection lockSection) {
		var lockSectionDTO = new LockSectionDTO();
		lockSectionDTO.setId(lockSection.getId());
		lockSectionDTO.setSection(lockSection.getSection());
		lockSectionDTO.setLockResponseDTO(toLockResponse(lockSection.getLock()));
		return lockSectionDTO;
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
		var maxPanelSize = configurationRepository.findAll().get(0).getPanelMaxSizeOfLocks();
		var panelsOfUser = getPanelsWhereUserHasLocks(user);
		var userLocks = lockRepository.findByUser(user);
		var facebookUserFriends = userService.getUserFacebookFriends();
		var facebookUserFriendsLocks = lockRepository.findAllByUserIdAndPrivateLockFalse(facebookUserFriends);
		panelsOfUser.parallelStream().forEach(panel -> addUserLocks(panel, userLocks));
		panelsOfUser.parallelStream().forEach(panel -> addFacebookUserFriendsLocks(panel, facebookUserFriendsLocks));
		panelsOfUser.parallelStream().forEach(panel -> addRandomLocksFromSameCountryToPanel(panel,
				maxPanelSize - (userLocks.size() + facebookUserFriendsLocks.size()), user));
		return panelsOfUser.parallelStream().map(this::toPanelDTO).collect(Collectors.toList());
	}

	private void addRandomLocksFromSameCountryToPanel(Panel panel, int remainedSlots, User user) {
		var randomLocks = lockRepository.findLocksRandomByCountry(remainedSlots, user.getId(), user.getCountry());
		for (Lock lock : randomLocks) {
			IN: for (LockSection section : panel.getLockSection()) {
				if (section.getLock() == null) {
					section.setLock(lock);
					break IN;
				}
			}
		}
	}

	private void addFacebookUserFriendsLocks(Panel panel, List<Lock> facebookUserFriendsLocks) {
		// TODO FINISH IMPLEMENTATION
	}

	private void addUserLocks(Panel panel, List<Lock> userLocks) {
		panel.setLockSection(lockSectionRepository.findAll());
		for (Lock lock : userLocks) {
			for (LockSection lockSection : panel.getLockSection()) {
				if (lockSection.getId().equals(lock.getLockSection().getId())) {
					lockSection.setLock(lock);
				}
			}
		}
	}

	private List<Panel> getPanelsWhereUserHasLocks(User user) {
		return panelRepository.findByUser(user.getId());
	}

	private Panel addPanelLocks(Panel panel) {
		// TODO FINISH IMPLEMENTATION
//		if (panel.getLocks() == null) {
//			panel.setLocks(lockRepository.findByPanelIdAndPaidTrue(panel.getId()));
//		} else {
//			panel.getLocks().addAll(lockRepository.findByPanelIdAndPaidTrue(panel.getId()));
//		}
		return panel;
	}
}
