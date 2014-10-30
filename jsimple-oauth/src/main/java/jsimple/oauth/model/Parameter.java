package jsimple.oauth.model;

import jsimple.oauth.utils.OAuthEncoder;
import org.jetbrains.annotations.Nullable;

/**
 * @author: Pablo Fernandez
 */
public class Parameter implements Comparable<Parameter> {
    private static final String UTF = "UTF8";

    private final String key;
    private final String value;

    public Parameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String asUrlEncodedPair() {
        //return OAuthEncoder.encode(key).concat("=").concat(OAuthEncoder.encode(value));
        return OAuthEncoder.encode(key) + "=" + OAuthEncoder.encode(value);
    }

    @Override public boolean equals(@Nullable Object other) {
        if (other == null)
            return false;
        if (other == this)
            return true;

        if (!(other instanceof Parameter))
            return false;
        Parameter otherParam = (Parameter) other;
        return otherParam.key.equals(key) && otherParam.value.equals(value);
    }

    @Override public int hashCode() {
        return key.hashCode() + value.hashCode();
    }

    public int compareTo(Parameter parameter) {
        int keyDiff = key.compareTo(parameter.key);

        return keyDiff != 0 ? keyDiff : value.compareTo(parameter.value);
    }
}
