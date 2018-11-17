package binar.box.service;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

	public PanelDTO getUserLocksAndPanel(Long panelId) {
		var user = userService.getAuthenticatedUser();
		var maxPanelSize = configurationRepository.findAll().get(0).getPanelMaxSizeOfLocks();
		var panel= panelRepository.findOne(panelId);
		var lockSections = panel.getLockSections();

		//TODO: when things get out of hand move this logic to sql query
		var userLocks = lockSections.stream()
				.map(LockSection::getLocks)
				.flatMap(List::stream)
				.filter(l-> l.getUser().equals(user))
				.collect(Collectors.toList());
		var facebookUserFriends = userService.getUserFacebookFriends();
		var facebookUserFriendsLocks = lockRepository.findAllByUserIdAndPrivateLockFalse(facebookUserFriends);

		addUserLocks(panel.getLockSections(), userLocks);
		addFacebookUserFriendsLocks(panel.getLockSections(), facebookUserFriendsLocks);
		var freePanelLockSection = maxPanelSize - (userLocks.size() + facebookUserFriendsLocks.size());
		if (freePanelLockSection > 0) {
			facebookUserFriends.add(user.getId());
			addRandomLocksFromSameCountryToPanel(panel.getLockSections(), freePanelLockSection, facebookUserFriends,
					user.getCountry());
		}
		return panelConveter.toDTO(panel);
	}

	private void addRandomLocksFromSameCountryToPanel(List<LockSection> lockSections, int remainedSlots, List<String> userIds,
			String country) {
		var randomLocks = lockRepository.findLocksRandomByCountry(remainedSlots, userIds, country);
		for (Lock lock : randomLocks) {
			for (LockSection lockSection : lockSections) {
				if (populateEmptySection(lockSection, lock))
					break;
			}
		}
	}

	private void addFacebookUserFriendsLocks(List<LockSection> lockSections, List<Lock> facebookUserFriendsLocks) {
		for (Lock lock : facebookUserFriendsLocks) {
			for (var sectionIndex = 0; sectionIndex < lockSections.size(); sectionIndex++) {
				if (lockSections.get(sectionIndex).getLocks().isEmpty()) {
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
			if (populateEmptySection(lockSection, lock))
				return;
			}
	}

	private boolean putLockAhead(List<LockSection> lockSections, int sectionIndex, Lock lock) {
		for (var index = sectionIndex; index < lockSections.size(); index++) {
			var lockSection = lockSections.get(index);
			if (populateEmptySection(lockSection, lock))
				return true;
		}
		return false;
	}

	private void addUserLocks(List<LockSection> lockSections, List<Lock> userLocks) {
		Random random = new Random();
		for (Lock lock : userLocks) {
			var lockSectionIndex = random.nextInt(lockSections.size());
			var lockSection = lockSections.get(lockSectionIndex);
			if (!populateEmptySection(lockSection, lock)) {
				for (var lockIndex = 0; lockSectionIndex < lockSections.size(); lockSectionIndex++) {
					var localLockSection = lockSections.get(lockIndex);
					populateEmptySection(localLockSection, lock);
				}
			}
		}
	}

	private boolean populateEmptySection(LockSection lockSection, Lock lock){
		if (lockSection.getLocks().isEmpty()) {
			lockSection.getLocks().add(lock);
			return true;
		}
		return false;
	}
}
