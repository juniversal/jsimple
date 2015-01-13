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

package goodfriends.model;

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

	public String toJson(String applicationUserId) {
		return "{\"name\":\"" + name + "\",\"picURL\":\"" + pictureUrl + "\",\"myid\":" + applicationUserId + "}";
	}

	public int compareToByName(FacebookFriend arg0) {
		return name.compareTo(arg0.name);
	}
}
