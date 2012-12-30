package jsimple.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Bret Johnson
 * @since 12/23/12 1:54 AM
 */
public class HttpRequestParams implements Iterable<String> {
    private ArrayList<String> names;
    private HashMap<String, String> values;

    public HttpRequestParams add(String name, String value) {
        if (values.containsKey(name))
            throw new RuntimeException("Parameter " + name + " already added");

        names.add(name);
        values.put(name, value);
        return this;
    }

    public String getValue(String name) {
        return values.get(name);
    }

    @Override public Iterator<String> iterator() {
        return names.iterator();
    }
}
