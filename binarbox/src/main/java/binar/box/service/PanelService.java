package binar.box.service;

import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import binar.box.converter.PanelConveter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import binar.box.domain.Lock;
import binar.box.domain.LockSection;
import binar.box.domain.Panel;
import binar.box.dto.PanelDTO;
import binar.box.repository.ConfigurationRepository;
import binar.box.repository.LockRepository;
import binar.box.repository.LockSectionRepository;
import binar.box.repository.PanelRepository;
import binar.box.util.Constants;
import binar.box.util.Exceptions.LockBridgesException;

@Service
@Transactional
public class PanelService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LockRepository lockRepository;
	@Autowired
	private PanelRepository panelRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ConfigurationRepository configurationRepository;
	@Autowired
	private LockSectionRepository lockSectionRepository;
	@Autowired
	private PanelConveter panelConveter;

	public List<PanelDTO> getAllPanels() {
		return panelConveter.toDTOList(panelRepository.findAll());
//		TO DO - ADD LOCKS TO PANEL
//		panels = panels.parallelStream().map(this::addPanelLocks).collect(Collectors.toList());
	}

	public PanelDTO getUserLocksAndPanels() {
		var user = userService.getAuthenticatedUser();
		var maxPanelSize = configurationRepository.findAll().get(0).getPanelMaxSizeOfLocks();
		var userPanel = panelRepository.findById(1L).orElseThrow(() -> new LockBridgesException(Constants.PANEL_NOT_FOUND));
		var userLocks = lockRepository.findByUser(user);
		var facebookUserFriends = userService.getUserFacebookFriends();
		var facebookUserFriendsLocks = lockRepository.findAllByUserIdAndPrivateLockFalse(facebookUserFriends);
		addUserLocks(userPanel, userLocks);
		addFacebookUserFriendsLocks(userPanel, facebookUserFriendsLocks);
		var freePanelLockSection = maxPanelSize - (userLocks.size() + facebookUserFriendsLocks.size());
		if (freePanelLockSection > 0) {
			facebookUserFriends.add(user.getId());
			addRandomLocksFromSameCountryToPanel(userPanel, freePanelLockSection, facebookUserFriends,
					user.getCountry());
		}
		return panelConveter.toDTO(userPanel);
	}

	private void addRandomLocksFromSameCountryToPanel(Panel panel, int remainedSlots, List<String> userIds,
			String country) {
		var randomLocks = lockRepository.findLocksRandomByCountry(remainedSlots, userIds, country);
		for (Lock lock : randomLocks) {
			for (LockSection section : panel.getLockSection()) {
				if (section.getLock() == null) {
					section.setLock(lock);
					break;
				}
			}
		}
	}

	private void addFacebookUserFriendsLocks(Panel panel, List<Lock> facebookUserFriendsLocks) {
		var lockSections = panel.getLockSection();
		for (Lock lock : facebookUserFriendsLocks) {
			for (var sectionIndex = 0; sectionIndex < lockSections.size(); sectionIndex++) {
				if (lockSections.get(sectionIndex).getLock() != null) {
					var foundSlot = putLockAhead(lockSections, sectionIndex, lock);
					if (!foundSlot) {
						putLockBehind(lockSections, sectionIndex, lock);
					}
					break;
				}

			}
		}
	}

	private void putLockBehind(List<LockSection> lockSections, int sectionIndex, Lock lock) {
		for (var index = sectionIndex; index >= 0; index--) {
			var lockSection = lockSections.get(index);
			if (lockSection.getLock() == null) {
				lockSection.setLock(lock);
				return;
			}
		}
	}

	private boolean putLockAhead(List<LockSection> lockSections, int sectionIndex, Lock lock) {
		for (var index = sectionIndex; index < lockSections.size(); index++) {
			var lockSection = lockSections.get(index);
			if (lockSection.getLock() == null) {
				lockSection.setLock(lock);
				return true;
			}
		}
		return false;
	}

	private void addUserLocks(Panel panel, List<Lock> userLocks) {
		panel.setLockSection(lockSectionRepository.findAll());
		Random random = new Random();
		for (Lock lock : userLocks) {
			var lockSectionIndex = random.nextInt(panel.getLockSection().size());
			var lockSection = panel.getLockSection().get(lockSectionIndex);
			if (lockSection.getLock() == null) {
				lockSection.setLock(lock);
			} else {
				for (var lockIndex = 0; lockSectionIndex < panel.getLockSection().size(); lockSectionIndex++) {
					var localLockSection = panel.getLockSection().get(lockIndex);
					if (localLockSection.getLock() == null) {
						localLockSection.setLock(lock);
					}
				}
			}
		}
	}
}
