package main;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import jsimple.io.JSimpleIO;
import jsimple.util.BasicException;
import jsimple.util.List;

import com.special_friend.facebook.FacebookAuthenticator;
import com.special_friend.facebook.FacebookConnector;
import com.special_friend.parsers.FacebookParser;
import com.special_friends.model.ApplicationModelEndpoint;
import com.special_friends.model.FacebookFriend;
import com.special_friends.model.FacebookPost;

public class Main {
	static Scanner keyboard = new Scanner(System.in);

	public static void main(String[] args) throws UnsupportedEncodingException {
		//========================bootstrap
		/*start jsimple*/
		JSimpleIO.init();
		boolean invalidCode = true;
		String facebookToken = "";
		/*app permission that we need in order to extract the facebook data*/
		String authPermissions = "user_friends,read_stream,read_friendlists";
		FacebookAuthenticator authenticator = new FacebookAuthenticator(authPermissions);
		String redirectUrl = authenticator.getAuthorizationUrl();
		//========================
		
		//========================facebook authentication
		System.out.println("Paste this in your browser , authenticate and coppy the <code> from the request");
		System.out.println("--> " + redirectUrl + "\n");
		/*get the facebook ouath code and validate it*/
		do {
			invalidCode = false;
			System.out.print("Insert facebook code : ");
			String authCode = keyboard.nextLine();
			try {
				/*get acesstoken from the facebook code , or print what was the error and try again*/
				facebookToken = authenticator.getAccessToken(authCode);
			} catch (BasicException e) {
				System.out.println("Error " + e.getMessage() + "\n");
				invalidCode = true;
			}
		} while (invalidCode);
		
		/*get the facebook data about the current user and save it in app endpoint*/
		FacebookConnector connector = new FacebookConnector(facebookToken);
		FacebookParser fbParser = new FacebookParser();
		String myInfo = connector.getMyInfo();
		String myName = fbParser.extractMyName(myInfo);
		String myFbID = fbParser.extractMyId(myInfo);
		System.out.println("Authentication Successful");
		System.out.println("You are " + myName + "[" + myFbID + "]\n");
		ApplicationModelEndpoint.getInstance().setMyID(myFbID);
		//========================
		
		//========================load the friendlist
		System.out.println("Where to load your saved friends?");
		System.out.print("1=cloud ; 2=localFile ; 3=don't load :");
		String input = null;
		do {
			input = keyboard.next();
		}
		while (!isValidInput(input, 1, 3));
		int in = new Integer(input);
		switch (in) {
		case 1:
			/*azure mobile*/
			ApplicationModelEndpoint.getInstance().loadFromCloud();
			break;
		case 2:
			/*local storage*/
			ApplicationModelEndpoint.getInstance().loadFromFile();
			break;
		default:
			break;
		}
		//========================
		
		//========================Print user's good friends and the full friend list extracted from facebook
		System.out.println("\nYour good friends are : ");
		List<FacebookFriend> goodFriends = ApplicationModelEndpoint.getInstance().getSpecialFriends();
		for (int i = 0; i < goodFriends.size(); i++) {
			System.out.println(i + ")" + goodFriends.get(i).getName());
		}
		
		System.out.println("-----------");
		System.out.println("Loading ....");

		System.out.println("All your friends are : ");
		List<FacebookFriend> fbFriends = fbParser.parserFriendList(connector.getFriendList());
		for (int i = 0; i < fbFriends.size(); i++) {
			System.out.println(i + ")" + fbFriends.get(i).getName());
		}
		//========================
		
		//======================== add friends from the facebook list to the special friend list
		Integer bigLoop = -1;
		while (true){
			System.out.print("\nAdd more (1=yes,2=no): ");
			do {
				input = keyboard.next();
			}
			while (!isValidInput(input, 1, 2));
			bigLoop = new Integer(input);
			
			if (bigLoop == 2){
				break;
			}
			do {
				System.out.print("The index of friend you want to add : ");
				input = keyboard.next();
			}while (!isValidInput(input, 0,fbFriends.size() ));
			
			in = new Integer(input);
			ApplicationModelEndpoint.getInstance().addSpecialFriend(fbFriends.get(in));
		}
		System.out.println("Done !\n");
		//========================
		
		//======================== reprint the special freinds (for validation)
		System.out.println("Your good friends are");
		goodFriends = ApplicationModelEndpoint.getInstance().getSpecialFriends();
		for (int i = 0; i < goodFriends.size(); i++) {
			System.out.println(i + ")" + goodFriends.get(i).getName());
		}
		//======================== 
		
		//======================== Extract your user's timeline from facebook and print only the posts from special friends
		System.out.println("Loading ....");
		/*how far into timeline history to go*/
		int postLimit = 100;
		/*full timeline as a json */
		String timeline = connector.getMyTimeline(postLimit);
		System.out.println("\nYour friends posts are : ");
		/*Parse and filter the timeline*/
		List<FacebookPost> fbPosts = ApplicationModelEndpoint.getInstance().getSpecialFriendPosts(timeline);
		for (FacebookPost facebookPost : fbPosts) {
			System.out.println(facebookPost);
		}
		//========================
		
		//======================== Save your newly added friends
		System.out.print("Do you want to save the fiend list (1=cloud,2=localFile,3=don't save) : ");
		do {
			input = keyboard.next();
		}
		while (!isValidInput(input, 0, 3));
		
		in = new Integer(input);
		
		if (in==1){
			ApplicationModelEndpoint.getInstance().saveToCloud();
		}else if (in==2){
			ApplicationModelEndpoint.getInstance().saveToFile();
		}
		//========================
		System.out.println("All done BB now");
	}

	
	/** Console input validator ,
	 * return true if input is a number and is in the range specified
	 */
	private static boolean isValidInput(String input ,int lowRange, int topRange){
		int item;
		try {
			item = new Integer(input);
		}catch (NumberFormatException e){
			System.out.println("Bad Option , not a number ");
			return false;
		}
		if (item<lowRange || item>  topRange){
			System.out.println("Bad Option , not in range ");
			return false;
		}
		return true;
	}
}
