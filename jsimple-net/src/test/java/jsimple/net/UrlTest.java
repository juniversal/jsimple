package jsimple.net;

import jsimple.unit.UnitTest;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

public class UrlTest extends UnitTest {
    @Test public void testUrl() {
        assertQueryStringMatches("foo&bar", "http://www.mit.edu?foo&bar#abc");
        assertQueryStringMatches("foo&bar", "http://www.mit.edu?foo&bar");
        assertQueryStringMatches("foo&bar?def", "http://www.mit.edu?foo&bar?def");
        assertQueryStringMatches("foo&bar?def", "http://www.mit.edu?foo&bar?def#abc#def");
        assertQueryStringMatches("", "http://www.mit.edu?");
        assertQueryStringMatches(null, "http://www.mit.edu");
    }

    public void assertQueryStringMatches(@Nullable String expectedQueryString, String urlString) {
        @Nullable String queryString = new Url(urlString).getQuery();

        if (expectedQueryString == null || queryString == null) {
            if (!(expectedQueryString == null && queryString == null))
                fail("One of queryString & expectedQueryString is null & the other not null");
        } else assertEquals(expectedQueryString, queryString);
    }
}
