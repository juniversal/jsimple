/*
 * Copyright (c) 2012-2014, Microsoft Mobile
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

package jsimple.net;

import jsimple.util.BasicException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * HttpRequestParams represents a set of string name/value pairs which can (eventually) be passed as URL or form
 * parameters for an HTTP request.  The entries are ordered--the order that they are added matches the order they are
 * returned from getNames.  The same name can't be added twice.
 *
 * @author Bret Johnson
 * @since 12/23/12 1:54 AM
 */
public class HttpRequestParams {
    private ArrayList<String> names = new ArrayList<String>();
    private HashMap<String, String> values = new HashMap<String, String>();

    public HttpRequestParams add(String name, String value) {
        if (values.containsKey(name))
            throw new BasicException("Parameter {} already added", name);

        names.add(name);
        values.put(name, value);
        return this;
    }

    public @Nullable String getValueOrNull(String name) {
        return values.get(name);
    }

    public String getValue(String name) {
        String value = values.get(name);
        if (value == null)
            throw new BasicException("Value {} not present in HTTPRequestParams", name);
        return value;
    }

    public List<String> getNames() {
        return names;
    }
}
