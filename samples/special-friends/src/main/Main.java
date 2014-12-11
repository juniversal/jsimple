package main;

import java.io.UnsupportedEncodingException;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.DebugGraphics;

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

	// local json
	//iteration on posts 
	public static void main(String[] args) throws UnsupportedEncodingException {
		// bootstrap :
		JSimpleIO.init();
		boolean invalidCode = true;
		String facebookToken = "";
		/****/
		String authPermissions = "user_friends,read_stream,read_friendlists";
		FacebookAuthenticator authenticator = new FacebookAuthenticator(authPermissions);
		String redirectUrl = authenticator.getAuthorizationUrl();
	
		
		System.out.println("Paste this in your browser , authenticate and coppy the <code> from the request");
		System.out.println("--> " + redirectUrl + "\n");

		do {
			invalidCode = false;
			System.out.print("Insert facebook code : ");
			String authCode = keyboard.nextLine();
			try {
				facebookToken = authenticator.getAcessToken(authCode);
			} catch (BasicException e) {
				System.out.println("Error " + e.getMessage() + "\n");
				invalidCode = true;
			}
		} while (invalidCode);
		
		FacebookConnector connector = new FacebookConnector(facebookToken);
		FacebookParser fbParser = new FacebookParser();
		String myInfo = connector.getMyInfo();
		String myName = fbParser.extractMyName(myInfo);
		String myFbID = fbParser.extractMyId(myInfo);
		System.out.println("Authentication Successful");
		System.out.println("You are " + myName + "[" + myFbID + "]\n");
		ApplicationModelEndpoint.getInstance().setMyID(myFbID);
		
		
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
			ApplicationModelEndpoint.getInstance().loadFromCloud();
			break;
		case 2:
			ApplicationModelEndpoint.getInstance().loadFromFile();
			break;
		default:
			break;
		}
		
		
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
		
		System.out.println("Your good friends are");
		goodFriends = ApplicationModelEndpoint.getInstance().getSpecialFriends();
		for (int i = 0; i < goodFriends.size(); i++) {
			System.out.println(i + ")" + goodFriends.get(i).getName());
		}
		
		System.out.println("Loading ....");
		String timeline = connector.getMyTimeline();
		System.out.println("Full timeline is" + timeline);
		System.out.println("\nYour friends posts are : ");
		List<FacebookPost> fbPosts = ApplicationModelEndpoint.getInstance().getSpecialFriendPosts(timeline);
		for (FacebookPost facebookPost : fbPosts) {
			System.out.println(facebookPost);
		}

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
		
		System.out.println("All done BB now");
	}

	
	
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
