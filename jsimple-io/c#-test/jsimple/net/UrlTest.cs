namespace jsimple.net {

    using UnitTest = jsimple.unit.UnitTest;

    using NUnit.Framework;

    public class UrlTest : UnitTest {
        [Test] public virtual void testUrl()
        {
            assertQueryStringMatches("foo&bar", "http://www.mit.edu?foo&bar#abc");
            assertQueryStringMatches("foo&bar", "http://www.mit.edu?foo&bar");
            assertQueryStringMatches("foo&bar?def", "http://www.mit.edu?foo&bar?def");
            assertQueryStringMatches("foo&bar?def", "http://www.mit.edu?foo&bar?def#abc#def");
            assertQueryStringMatches("", "http://www.mit.edu?");
            assertQueryStringMatches(null, "http://www.mit.edu");
        }

        public virtual void assertQueryStringMatches(string expectedQueryString, string urlString) {
            string queryString = (new Url(urlString)).Query;

            if (expectedQueryString == null || queryString == null) {
                if (!(expectedQueryString == null && queryString == null))
                    fail("One of queryString & expectedQueryString is null & the other not null");
            }
            else
                assertEquals(expectedQueryString, queryString);
        }
    }

}