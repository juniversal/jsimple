package jsimple.net;

import org.jetbrains.annotations.Nullable;

/**
 * @author Bret Johnson
 * @since 10/7/12 3:28 AM
 */
public class Url {
    private String url;

    public Url(String url) {
        this.url = url;
    }

    /**
     * Return the query part of the URL or null if there is no query party.  For example:
     * <p/>
     * http://www.acme.com/abc?foo=3&bar=4#abcdef  =>  foo=3&bar=4
     * <p/>
     * http://www.acme.com/abc  =>  null
     *
     * @return query part of URL or null
     */
    public @Nullable String getQuery() {
        int questionMarkIndex = url.indexOf('?');
        if (questionMarkIndex == -1)
            return null;
        else {
            int queryStartIndex = questionMarkIndex + 1;

            int hashIndex = url.indexOf('#', queryStartIndex);
            if (hashIndex == -1)
                return url.substring(queryStartIndex);
            else return url.substring(queryStartIndex, hashIndex);
        }
    }
}
