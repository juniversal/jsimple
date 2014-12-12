package com.special_friends.model;

public class FacebookFriend {
	private String name;
	private String pictureUrl;

	public FacebookFriend(String name, String pictureUrl) {
		this.name = name;
		this.pictureUrl = pictureUrl;
	}

	@Override
	public String toString() {
		return "[name=" + name + pictureUrl + "]\n";
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof FacebookFriend)) {
			return false;
		}
		return ((FacebookFriend) arg0).getName().equals(this.name);
	}

	public String toJson() {
		return "{\"name\":\"" + name + "\",\"picURL\":\"" + pictureUrl + "\",\"myid\":" + ApplicationModelEndpoint.getInstance().getMyID() + "}";
	}
}
