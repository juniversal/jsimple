package jsimple.util;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * Created by Bret on 4/19/2014.
 */
public class TextualPathTest extends UnitTest {
    @Test public void testStartsWith() throws Exception {
        TextualPath p1 = new TextualPath("aaa");
        TextualPath p2 = new TextualPath("aaa", "bbb");
        TextualPath p3 = new TextualPath("aaa", "bbb", "ccc");

        assertTrue(new TextualPath("aaa", "bbb").startsWith(new TextualPath("aaa")));
        assertTrue(new TextualPath("aaa", "bbb").startsWith(new TextualPath("aaa", "bbb")));
        assertTrue(new TextualPath("aaa", "bbb").startsWith(new TextualPath()));

        assertFalse(new TextualPath("aaa", "bbb").startsWith(new TextualPath("aaa", "ccc")));
        assertFalse(new TextualPath("aaa", "bbb").startsWith(new TextualPath("aaa", "bbb", "ccc")));
        assertFalse(new TextualPath("aaa", "bbb").startsWith(new TextualPath("bbb", "bbb", "ccc")));
    }
}
