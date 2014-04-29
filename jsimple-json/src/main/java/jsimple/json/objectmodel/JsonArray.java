package jsimple.json.objectmodel;

import java.util.ArrayList;

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
