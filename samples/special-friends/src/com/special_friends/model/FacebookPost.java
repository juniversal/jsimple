package com.special_friends.model;

public class FacebookPost {
	private String from;
	private String story;
	private String picture;
	private String link;
	private String name;
	private String description;
	private String message;

	public FacebookPost(String from, String story, String picture, String link, String name, String description,String message) {
		super();
		this.from = from;
		this.story = story;
		this.picture = picture;
		this.link = link;
		this.name = name;
		this.message = message;
		this.description = description;
	}

	public String getFrom() {
		return from;
	}

	public String getStory() {
		return story;
	}

	public String getPicture() {
		return picture;
	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Facebook Post : \n");
		output.append("*From : "+from+"\n");
		if (name!=null){
			output.append("*Name : "+name+"\n");
		}
		if (description!=null){
			output.append("*Description : "+description+"\n");
		}
		if (story!=null){
			output.append("*Story : "+story+"\n");
		}
		if (message!=null){
			output.append("*Message : "+message+"\n");
		}
		
		return output.toString();
	}
}
