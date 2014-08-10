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

package jsimple.json.objectmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bret Johnson
 * @since 5/7/12 12:40 AM
 */
public final class JsonArray extends JsonObjectOrArray {
    private ArrayList<Object> values = new ArrayList<Object>();

    /**
     * Return the number of values in the array.
     *
     * @return count of values in the array
     */
    public int size() {
        return values.size();
    }

    /**
     * Return the value at the specified index.  If the index is out of range, an exception is thrown.
     *
     * @param index index of item in array
     * @return value at specified index
     */
    public Object get(int index) {
        return values.get(index);
    }

    public boolean getBoolean(int index) {
        return (boolean) (Boolean) get(index);
    }

    public String getString(int index) {
        return (String) get(index);
    }

    public int getInt(int index) {
        return (int) (Integer) get(index);
    }

    public long getLong(int index) {
        return (long) (Long) get(index);
    }

    public double getDouble(int index) {
        return (double) (Double) get(index);
    }

    public JsonObject getJsonObject(int index) {
        return (JsonObject) get(index);
    }

    public JsonArray getJsonArray(int index) {
        return (JsonArray) get(index);
    }

    /**
     * Add the specified value to the array, at the end.
     *
     * @param value value to add
     */
    public JsonArray add(Object value) {
        values.add(value);
        return this;
    }
}
