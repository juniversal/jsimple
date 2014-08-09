/*
    Copyright (c) 2012-2014, Microsoft Mobile

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/

package jsimple.json.objectmodel;

import jsimple.json.JsonException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A JsonObject represents an ordered list of name/value pairs.  Note that unlike a HashMap, the list is ordered, so
 * that the order in the serialized text can be controlled.  For now, queries just do a linear search, but we might
 * create a HashMap later, say if there are > 5 items in the object, for performance.  There's currently no prohibition
 * against a name being duplicated in the object; if that happens the value of the first occurrence is returned
 * currently but future implementations may change that.
 *
 * @author Bret Johnson
 * @since 5/6/12 12:29 AM
 */
public final class JsonObject extends JsonObjectOrArray {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Object> values = new ArrayList<>();

    /**
     * Get number of name/value pairs in the object.
     *
     * @return number of name/value pairs
     */
    public int size() {
        return names.size();
    }

    /**
     * Get name at the specified index.
     *
     * @param index index in question; must be < size()
     * @return name at that index
     */
    public String getName(int index) {
        return names.get(index);
    }

    /**
     * Get value at the specified index.
     *
     * @param index index in question; must be < size()
     * @return value at that index
     */
    public Object getValue(int index) {
        return values.get(index);
    }

    /**
     * Return the value of the specified key.  If the key isn't present, an exception is thrown.  If the key is present
     * and explicitly has the value null, then the JsonNull singleton is returned. Technically, nothing prevents a key
     * being duplicated in a JSON file; if that happens, the value of the first occurrence is returned here.
     *
     * @param name key name
     * @return value of key
     */
    public Object get(String name) {
        int length = names.size();

        for (int i = 0; i < length; ++i) {
            if (names.get(i).equals(name))
                return values.get(i);
        }

        throw new JsonException("JSON object is expected to have a value for {} but doesn't", name);
    }

    /**
     * Return the value of the specified key.  If the key isn't present, null is returned.  If the key is present and
     * explicitly has the value null, then the JsonNull singleton is returned. Technically, nothing prevents a key being
     * duplicated in a JSON file; if that happens, the value of the first occurrence is returned here.
     *
     * @param name key name
     * @return value of key
     */
    public @Nullable Object getOrNull(String name) {
        int length = names.size();

        for (int i = 0; i < length; ++i) {
            if (names.get(i).equals(name))
                return values.get(i);
        }

        return null;
    }

    public boolean getBoolean(String name) {
        return (boolean) (Boolean) get(name);
    }

    public @Nullable Boolean getBooleanOrNull(String name) {
        return (Boolean) getOrNull(name);
    }

    public boolean getBooleanOrDefault(String name, boolean defaultValue) {
        @Nullable Boolean value = getBooleanOrNull(name);
        return value == null ? defaultValue : (boolean) value;
    }

    public String getString(String name) {
        return (String) get(name);
    }

    public @Nullable String getStringOrNull(String name) {
        return (String) getOrNull(name);
    }

    public String getStringOrDefault(String name, String defaultValue) {
        @Nullable String value = getStringOrNull(name);
        return value == null ? defaultValue : (String) value;
    }

    public int getInt(String name) {
        return (int) (Integer) get(name);
    }

    public @Nullable Integer getIntOrNull(String name) {
        return (Integer) getOrNull(name);
    }

    public int getIntOrDefault(String name, int defaultValue) {
        @Nullable Integer value = getIntOrNull(name);
        return value == null ? defaultValue : (int) value;
    }

    public long getLong(String name) {
        Object value = get(name);
        if (value instanceof Integer)
            return (long) (Integer) value;
        else return (long) (Long) value;
    }

    public @Nullable Long getLongOrNull(String name) {
        @Nullable Object value = getOrNull(name);
        if (value == null)
            return null;
        else if (value instanceof Integer)
            return (long) (Integer) value;
        else return (Long) value;
    }

    public long getLongOrDefault(String name, long defaultValue) {
        @Nullable Long value = getLongOrNull(name);
        return value == null ? defaultValue : (long) value;
    }

    public double getDouble(String name) {
        return (double) (Double) get(name);
    }

    public @Nullable Double getDoubleOrNull(String name) {
        return (Double) getOrNull(name);
    }

    public double getDoubleOrDefault(String name, double defaultValue) {
        @Nullable Double value = getDoubleOrNull(name);
        return value == null ? defaultValue : (double) value;
    }

    public JsonObject getJsonObject(String name) {
        return (JsonObject) get(name);
    }

    public @Nullable JsonObject getJsonObjectOrNull(String name) {
        return (JsonObject) getOrNull(name);
    }

    public JsonArray getJsonArray(String name) {
        return (JsonArray) get(name);
    }

    public @Nullable JsonArray getJsonArrayOrNull(String name) {
        return (JsonArray) getOrNull(name);
    }

    /**
     * Return true if the object contains the specified key, false otherwise.  Like a Java HashMap, calling containsKey
     * is the only way to distinguish between a key not being present and it being present but explicitly set to null in
     * the JSON as the getOrNull method returns null in both cases here.
     *
     * @param keyName key keyName
     * @return true iff the object contains the key (even if its value is null)
     */
    public boolean containsKey(String keyName) {
        for (String name : names)
            if (name.equals(keyName))
                return true;
        return false;
    }

    public JsonObject add(String name, Object value) {
        names.add(name);
        values.add(value);
        return this;
    }

    public JsonObject addChildObject(String name) {
        JsonObject childObject = new JsonObject();
        names.add(name);
        values.add(childObject);
        return childObject;
    }

    public JsonArray addChildArray(String name) {
        JsonArray childArray = new JsonArray();
        names.add(name);
        values.add(childArray);
        return childArray;
    }
}
