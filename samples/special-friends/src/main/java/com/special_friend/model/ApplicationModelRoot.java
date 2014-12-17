package main.java.com.special_friend.model;

import jsimple.util.ArrayList;
import jsimple.util.List;
import jsimple.util.PlatformUtils;

/**
 * Application facade. Most of the app's functionality is exposed here
 */
public class ApplicationModelRoot {
	private final static ApplicationModelRoot instance = new ApplicationModelRoot();

	private List<FacebookFriend> specialFriends;

	public static ApplicationModelRoot getInstance() {
		return instance;
	}

	public ApplicationModelRoot() {
		specialFriends = new ArrayList<FacebookFriend>();
	}

	public void addSpecialFriend(FacebookFriend friend) {
		if (!specialFriends.contains(friend)) {
			specialFriends.add(friend);
		}
	}

	public void addSpecialFriends(List<FacebookFriend> friends) {
		for (FacebookFriend facebookFriend : friends) {
			if (!specialFriends.contains(facebookFriend)) {
				specialFriends.add(facebookFriend);
			}
		}
	}

	public List<FacebookFriend> getSpecialFriends() {
		return specialFriends;
	}
}
