/*
 * Copyright (c) 2012-2015, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package goodfriends.util;

public class Constants {
	//what is you application called in windows-mobile
	public static final String AZURE_MOBILE_APP_NAME = "jsimple2";
	//the unique azure-mobile id (in ms-mobile dashboard: mobileServices->configure->manageKeys
	public static final String AZURE_APP_ID = "ddornXekBtfSlFlscingToOuvXWsOh51";
	//<app name> + preset db location
	public static final String AZURE_DB_PATH = "https://"+AZURE_MOBILE_APP_NAME+".azure-mobile.net/tables/";
	//example specific , a table that we store friends into
	public static final String AZURE_FRIEND_TABLE_NAME = "Friend";
	//example specific , user foreign key
	public static final String MY_ID_COLUMN = "myid";
	
	public static final String FACEBOOK_APP_ID = "310019222529140";
	public static final String FACEBOOK_APP_SECRET = "86b41a3678ef76807c8e799c0b31e129";
	public static final String FACEBOOK_SCOPE = "user_friends,read_stream,read_friendlists";
	//after auth is done , facebook will redirect you to this url, the redirect request will contain
	//the access code
	//use localhost of www.facebook.com for redirect. 
	//If you have a web service , you can redirect to that BUT dont forget to set that url as a site url in facebook app settings 
	public static final String FACEBOOK_CALLBACK = "http://localhost:8080/";
	public static final String LOCAL_TOKEN_STORAGE = "tokenFile";
}