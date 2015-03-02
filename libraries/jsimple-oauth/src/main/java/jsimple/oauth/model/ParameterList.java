/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2015 Microsoft Mobile.  All Rights Reserved.
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
 *
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
import jsimple.util.*;
import org.jetbrains.annotations.Nullable;

/**
 * @author: Pablo Fernandez
 */
public class ParameterList {
    private static final char QUERY_STRING_SEPARATOR_CHAR = '?';
    private static final String QUERY_STRING_SEPARATOR = "?";
    private static final char PARAM_SEPARATOR_CHAR = '&';
    private static final char PAIR_SEPARATOR_CHAR = '=';
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
        for (MapEntry<String, String> entry : map.entrySet()) {
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
            url += url.indexOf(QUERY_STRING_SEPARATOR_CHAR) != -1 ? Characters.toString(PARAM_SEPARATOR_CHAR) : QUERY_STRING_SEPARATOR;
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
        if (! Strings.isNullOrEmpty(queryString)) {
            for (String param : Strings.split(queryString, PARAM_SEPARATOR_CHAR)) {
                List<String> pair = Strings.split(param, PAIR_SEPARATOR_CHAR);
                String key = OAuthEncoder.decode(pair.get(0));
                String value = pair.size() > 1 ? OAuthEncoder.decode(pair.get(1)) : EMPTY_STRING;
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
        sorted.parameters.sortInPlace(new Comparator<Parameter>() {
            @Override public int compare(Parameter paramter1, Parameter parameter2) {
                return paramter1.compareTo(parameter2);
            }
        });

        return sorted;
    }
}
