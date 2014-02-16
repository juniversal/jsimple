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
