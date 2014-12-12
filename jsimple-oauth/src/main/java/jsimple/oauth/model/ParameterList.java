/*
 * Portions copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from the Scribe Java OAuth
 * library https://github.com/fernandezpablo85/scribe-java (collectively, "Third
 * Party Code"). Microsoft Mobile is not the original author of the Third Party
 * Code.
 *
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

package jsimple.oauth.model;

import jsimple.oauth.utils.OAuthEncoder;
import jsimple.util.PlatformUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Pablo Fernandez
 */
public class ParameterList {
    private static final char QUERY_STRING_SEPARATOR_CHAR = '?';
    private static final String QUERY_STRING_SEPARATOR = "?";
    private static final String PARAM_SEPARATOR = "&";
    private static final String PAIR_SEPARATOR = "=";
    private static final String EMPTY_STRING = "";

    private final ArrayList<Parameter> parameters;

    public ParameterList() {
        parameters = new ArrayList<Parameter>();
    }

    ParameterList(List<Parameter> parameters) {
        this.parameters = new ArrayList<Parameter>(parameters);
    }

    public ParameterList(Map<String, String> map) {
        this();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            parameters.add(new Parameter(entry.getKey(), entry.getValue()));
        }
    }

    public void add(String key, String value) {
        parameters.add(new Parameter(key, value));
    }

    public String appendTo(String url) {
        assert url != null : "Cannot append to null URL";

        String queryString = asFormUrlEncodedString();
        if (queryString.equals(EMPTY_STRING)) {
            return url;
        } else {
            url += url.indexOf(QUERY_STRING_SEPARATOR_CHAR) != -1 ? PARAM_SEPARATOR : QUERY_STRING_SEPARATOR;
            url += queryString;
            return url;
        }
    }

    public String asOauthBaseString() {
        return OAuthEncoder.encode(asFormUrlEncodedString());
    }

    public String asFormUrlEncodedString() {
        if (parameters.size() == 0)
            return EMPTY_STRING;

        StringBuilder builder = new StringBuilder();
        for (Parameter p : parameters) {
            builder.append('&');
            builder.append(p.asUrlEncodedPair());
        }

        return builder.toString().substring(1);
    }

    public void addAll(ParameterList other) {
        parameters.addAll(other.parameters);
    }

    public void addQueryString(@Nullable String queryString) {
        if (queryString != null && queryString.length() > 0) {
            for (String param : queryString.split(PARAM_SEPARATOR)) {
                String[] pair = param.split(PAIR_SEPARATOR);
                String key = OAuthEncoder.decode(pair[0]);
                String value = pair.length > 1 ? OAuthEncoder.decode(pair[1]) : EMPTY_STRING;
                parameters.add(new Parameter(key, value));
            }
        }
    }

    public boolean contains(Parameter param) {
        return parameters.contains(param);
    }

    public int size() {
        return parameters.size();
    }

    public ParameterList sort() {
        ParameterList sorted = new ParameterList(parameters);
        // TODO (Bret): This doesn't seem exactly right--the OAuth spec says that parameters should be sorted in byte order
        PlatformUtils.sortList(sorted.parameters);
        return sorted;
    }
}
