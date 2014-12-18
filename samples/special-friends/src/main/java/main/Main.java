package main.java.main;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import jsimple.io.JSimpleIO;
import jsimple.oauth.model.Token;
import jsimple.util.ArrayList;
import jsimple.util.BasicException;
import jsimple.util.List;
import jsimple.util.PlatformUtils;
import main.java.com.special_friend.azure.AzureMobileFacade;
import main.java.com.special_friend.facebook.FacebookFacade;
import main.java.com.special_friend.local.LocalFileFacade;
import main.java.com.special_friend.model.ApplicationModelRoot;
import main.java.com.special_friend.model.FacebookFriend;
import main.java.com.special_friend.model.FacebookPost;


public class Main {
	static Scanner keyboard = new Scanner(System.in);

	public static void main(String[] args) throws UnsupportedEncodingException {
		// ========================bootstrap
		/* start jsimple */
		JSimpleIO.init();
		Token fbToken = null;
		int in = -1;
		FacebookFacade fbFacade = new FacebookFacade();
		AzureMobileFacade azureFacade = new AzureMobileFacade();
		LocalFileFacade localFacade = new LocalFileFacade();
		// ========================

		// ========================refresh token or get a new one
		System.out.println("Do you want to refresh your previous token or get a new one?");
		
		in = Main.getInput(1, 2,"1=refresh ; 2=get new:");
		if (in == 1) {
			/*refresh old token*/
			fbToken = localFacade.loadTokenLocally();
			fbToken = fbFacade.refreshToken(fbToken);
		} else {
			/* get the facebook ouath code and validate it */
			System.out.println("Paste this in your browser , authenticate and coppy the <code> from the request");
			System.out.println("--> " + fbFacade.getAuthorizationUrl() + "\n");
			
			boolean invalidCode = false;
			do {
				invalidCode = false;
				System.out.print("Insert facebook code : ");
				String authCode = keyboard.next();
				try {
					/*get acesstoken from the facebook code , or print what was the error and try again*/
					fbToken = fbFacade.getAcessToken(authCode);
				} catch (BasicException e) {
					System.out.println("Error " + e.getMessage() + "\n");
					invalidCode = true;
				}
			} while (invalidCode);
			localFacade.saveToken(fbToken);
		}
		// ========================facebook authentication
		// get the facebook data about the current user and save it in app
		// endpoint
		// we are done with the oauth, now we can use the access token as a
		// string instead of an jsimple.oauth.model.Token
		fbFacade.setConnectorAuthToken(fbToken.getTokenString());
		String myName = fbFacade.getMyFbName();
		String myFbID = fbFacade.getMyFbId();
		System.out.println("Authentication Successful");
		System.out.println("You are " + myName + "[" + myFbID + "]\n");
		// ========================

		// ========================load the friendlist
		System.out.println("Where to load your saved friends?");
		List<FacebookFriend> friends = null;
		in = getInput(1, 3,"1=cloud ; 2=localFile ; 3=don't load :");
		
		switch (in) {
			case 1:
				/* azure mobile */
				friends = azureFacade.loadFriends(myFbID);
				break;
			case 2:
				/* local storage */
				friends = localFacade.selectFriends(myFbID);
				break;
			default:
				/*do not load*/
				friends = new ArrayList<FacebookFriend>();
				break;
		}
		ApplicationModelRoot.getInstance().addSpecialFriends(friends);
		// ========================

		// ========================Print user's good friends and the full friend
		List<FacebookFriend> goodFriends = ApplicationModelRoot.getInstance().getSpecialFriends();

		if (ApplicationModelRoot.getInstance().getSpecialFriends().size() != 0) {
			System.out.println("\nYour good friends are : ");
			for (int i = 0; i < goodFriends.size(); i++) {
				System.out.println(i + ")" + goodFriends.get(i).getName());
			}
		} else {
			System.out.println("You don't have any special friends");
		}

		System.out.println("-----------");
		System.out.println("Loading ....");
		System.out.println("All your friends are : ");
		
		
		List<FacebookFriend> fbFriends = fbFacade.getAllFbFriends();
		Main.sortFriendList(fbFriends);
		
		for (int i = 0; i < fbFriends.size(); i++) {
			System.out.println(i + ")" + fbFriends.get(i).getName());
		}
		// ========================

		// ======================== add friends from the facebook list to the
		// special friend list
		int addMore = -1;
		while (true) {
			addMore = getInput(1, 2,"\nAdd more (1=yes,2=no): ");
			if (addMore == 2) {
				break;
			}
			in = getInput(0, fbFriends.size(), "\nThe index of friend you want to add : ");
			ApplicationModelRoot.getInstance().addSpecialFriend(fbFriends.get(in));
		}
		System.out.println("Done !\n");
		// ========================

		// ======================== reprint the special freinds (for validation)
		goodFriends = ApplicationModelRoot.getInstance().getSpecialFriends();
		if (ApplicationModelRoot.getInstance().getSpecialFriends().size() != 0) {
			System.out.println("\nYour good friends are : ");
			for (int i = 0; i < ApplicationModelRoot.getInstance().getSpecialFriends().size(); i++) {
				System.out.println(i + ")" + goodFriends.get(i).getName());
			}
		} else {
			System.out.println("You don't have any special friends");
		}
		// ========================

		// ======================== Extract your user's timeline from facebook
		// and print only the posts from special friends
		System.out.println("Loading ....");
		/* how far into timeline history to go */
		// facebook uses algorithms with inconsistent behavior to post the feeds,
		// so to exactly the same request given at the exact moment can return distinct posts.
		// by passing a low post limit and a high page iteration count , we get more of the feeds than
		// if we would be passing a high limit and a low page iteration count. 
		int postLimit = 20;
		/* filtered timeline*/
		List<FacebookPost> fbPosts = fbFacade.getMyFilteredWall(postLimit,100, ApplicationModelRoot.getInstance().getSpecialFriends());
		if (fbPosts.size() != 0) {
			System.out.println("\nYour friends posts are : ");
			for (FacebookPost facebookPost : fbPosts) {
				System.out.println(facebookPost);
			}
		} else {
			System.out.println("No recent posts from special friends");
		}
		// ========================

		// ======================== Save your newly added friends
		in = getInput(1, 3, "Do you want to save the fiend list (1=cloud,2=localFile,3=don't save) : ");

		if (in == 1) {
			azureFacade.saveFriends(ApplicationModelRoot.getInstance().getSpecialFriends(), myFbID);
		} else if (in == 2) {
			localFacade.saveFriends(ApplicationModelRoot.getInstance().getSpecialFriends(), myFbID);
		}
		// ========================
		System.out.println("All done.");
	}

	/**
	 * Console input validator , return true if input is a number and is in the
	 * range specified
	 */
	private static boolean isValidInput(String input, int lowRange, int topRange) {
		int item;
		try {
			item = new Integer(input);
		} catch (NumberFormatException e) {
			System.out.println("Bad Option , not a number ");
			return false;
		}
		if (item < lowRange || item > topRange) {
			System.out.println("Bad Option , not in range ");
			return false;
		}
		return true;
	}

	private static int getInput(int lowRange, int topRange,String label) {
		String input = null;
		do {
			System.out.print(label);
			input = keyboard.next();
		} while (!isValidInput(input, lowRange, topRange));
		return new Integer(input);
	}
	
	private static void sortFriendList(List<FacebookFriend> original) {
		java.util.List<FacebookFriend> list = new java.util.ArrayList<FacebookFriend>();
		for (FacebookFriend facebookFriend : original) {
			list.add(facebookFriend);
		}
		PlatformUtils.sortList(list);
		original.clear();

		for (FacebookFriend facebookFriend : list) {
			original.add(facebookFriend);
		}
	}
}
