package jsimple.oauth.utils;

import java.util.Map;

/**
 * @author: Pablo Fernandez
 */
public class MapUtils {
    public static <K, V> String toStringFromMap(Map<K, V> map) {
        if (map == null) return "";
        if (map.isEmpty()) return "{}";

        StringBuilder result = new StringBuilder();
        result.append("{");

        boolean addedSomething = false;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (addedSomething)
                result.append(", ");
            else addedSomething = true;

            result.append(entry.getKey().toString());
            result.append(" -> ");
            result.append(entry.getValue().toString());

            result.append(String.format(", %s -> %s ", entry.getKey().toString(), entry.getValue().toString()));
        }

        result.append("}");

        return result.toString();
    }
}
