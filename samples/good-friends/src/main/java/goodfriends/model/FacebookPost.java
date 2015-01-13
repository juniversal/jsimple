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

public class FacebookPost {
    private String from;
    private String story;
    private String picture;
    private String link;
    private String name;
    private String description;
    private String message;

    public FacebookPost(String from, String story, String picture, String link, String name, String description, String message) {
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
        output.append("*From : " + from + "\n");
        if (name != null) {
            output.append("*Name : " + name + "\n");
        }
        if (description != null) {
            output.append("*Description : " + description + "\n");
        }
        if (story != null) {
            output.append("*Story : " + story + "\n");
        }
        if (message != null) {
            output.append("*Message : " + message + "\n");
        }

        return output.toString();
    }
}
