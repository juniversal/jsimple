/**
 * Copyright (c) 2004-2011 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package jsimple.util;

import jsimple.unit.UnitTest;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Ceki Gulcu
 * @author Bret Johnson modified for JSimple
 */
public class MessageFormatterTest extends UnitTest {
    Integer i1 = new Integer(1);
    Integer i2 = new Integer(2);
    Integer i3 = new Integer(3);
    Integer[] ia0 = new Integer[]{i1, i2, i3};
    Integer[] ia1 = new Integer[]{new Integer(10), new Integer(20), new Integer(30)};

    String result;

    @Test public void testNullParam() {
        result = MessageFormatter.format("Value is {}.", null).getFormattedMessage();
        assertEquals("Value is null.", result);

        result = MessageFormatter.format("Val1 is {}, val2 is {}.", null, null)
                .getFormattedMessage();
        assertEquals("Val1 is null, val2 is null.", result);

        result = MessageFormatter.format("Val1 is {}, val2 is {}.", i1, null)
                .getFormattedMessage();
        assertEquals("Val1 is 1, val2 is null.", result);

        result = MessageFormatter.format("Val1 is {}, val2 is {}.", null, i2)
                .getFormattedMessage();
        assertEquals("Val1 is null, val2 is 2.", result);

        result = MessageFormatter.arrayFormat("Val1 is {}, val2 is {}, val3 is {}",
                new Integer[]{null, null, null}).getFormattedMessage();
        assertEquals("Val1 is null, val2 is null, val3 is null", result);

        result = MessageFormatter.arrayFormat("Val1 is {}, val2 is {}, val3 is {}",
                new Integer[]{null, i2, i3}).getFormattedMessage();
        assertEquals("Val1 is null, val2 is 2, val3 is 3", result);

        result = MessageFormatter.arrayFormat("Val1 is {}, val2 is {}, val3 is {}",
                new Integer[]{null, null, i3}).getFormattedMessage();
        assertEquals("Val1 is null, val2 is null, val3 is 3", result);
    }

    @Test public void testOneParameter() {
        result = MessageFormatter.format("Value is {}.", i3).getFormattedMessage();
        assertEquals("Value is 3.", result);

        result = MessageFormatter.format("Value is {", i3).getFormattedMessage();
        assertEquals("Value is {", result);

        result = MessageFormatter.format("{} is larger than 2.", i3).getFormattedMessage();
        assertEquals("3 is larger than 2.", result);

        result = MessageFormatter.format("No subst", i3).getFormattedMessage();
        assertEquals("No subst", result);

        result = MessageFormatter.format("Incorrect {subst", i3).getFormattedMessage();
        assertEquals("Incorrect {subst", result);

        result = MessageFormatter.format("Value is {bla} {}", i3).getFormattedMessage();
        assertEquals("Value is {bla} 3", result);

        result = MessageFormatter.format("Escaped \\{} subst", i3).getFormattedMessage();
        assertEquals("Escaped {} subst", result);

        result = MessageFormatter.format("{Escaped", i3).getFormattedMessage();
        assertEquals("{Escaped", result);

        result = MessageFormatter.format("\\{}Escaped", i3).getFormattedMessage();
        assertEquals("{}Escaped", result);

        result = MessageFormatter.format("File name is {{}}.", "App folder.zip").getFormattedMessage();
        assertEquals("File name is {App folder.zip}.", result);

        // escaping the escape character
        result = MessageFormatter.format("File name is C:\\\\{}.", "App folder.zip").getFormattedMessage();
        assertEquals("File name is C:\\App folder.zip.", result);
    }

    @Test public void testTwoParameters() {
        result = MessageFormatter.format("Value {} is smaller than {}.", i1, i2).getFormattedMessage();
        assertEquals("Value 1 is smaller than 2.", result);

        result = MessageFormatter.format("Value {} is smaller than {}", i1, i2).getFormattedMessage();
        assertEquals("Value 1 is smaller than 2", result);

        result = MessageFormatter.format("{}{}", i1, i2).getFormattedMessage();
        assertEquals("12", result);

        result = MessageFormatter.format("Val1={}, Val2={", i1, i2).getFormattedMessage();
        assertEquals("Val1=1, Val2={", result);

        result = MessageFormatter.format("Value {} is smaller than \\{}", i1, i2).getFormattedMessage();
        assertEquals("Value 1 is smaller than {}", result);

        result = MessageFormatter.format("Value {} is smaller than \\{} tail", i1, i2).getFormattedMessage();
        assertEquals("Value 1 is smaller than {} tail", result);

        result = MessageFormatter.format("Value {} is smaller than \\{", i1, i2).getFormattedMessage();
        assertEquals("Value 1 is smaller than \\{", result);

        result = MessageFormatter.format("Value {} is smaller than {tail", i1, i2).getFormattedMessage();
        assertEquals("Value 1 is smaller than {tail", result);

        result = MessageFormatter.format("Value \\{} is smaller than {}", i1, i2).getFormattedMessage();
        assertEquals("Value {} is smaller than 1", result);
    }

    @Test public void testExceptionIn_toString() {
        Object o = new Object() {
            public String toString() {
                throw new IllegalStateException("a");
            }
        };
        result = MessageFormatter.format("Troublesome object {}", o).getFormattedMessage();
        assertEquals("Troublesome object [FAILED toString(); toString threw exception: java.lang.IllegalStateException: a]", result);

    }

    // tests the case when the parameters are supplied in a single array
    @Test public void testArrayFormat() {
        result = MessageFormatter.arrayFormat("Value {} is smaller than {} and {}.", ia0).getFormattedMessage();
        assertEquals("Value 1 is smaller than 2 and 3.", result);

        result = MessageFormatter.arrayFormat("{}{}{}", ia0).getFormattedMessage();
        assertEquals("123", result);

        result = MessageFormatter.arrayFormat("Value {} is smaller than {}.", ia0).getFormattedMessage();
        assertEquals("Value 1 is smaller than 2.", result);

        result = MessageFormatter.arrayFormat("Value {} is smaller than {}", ia0).getFormattedMessage();
        assertEquals("Value 1 is smaller than 2", result);

        result = MessageFormatter.arrayFormat("Val={}, {, Val={}", ia0).getFormattedMessage();
        assertEquals("Val=1, {, Val=2", result);

        result = MessageFormatter.arrayFormat("Val={}, {, Val={}", ia0).getFormattedMessage();
        assertEquals("Val=1, {, Val=2", result);

        result = MessageFormatter.arrayFormat("Val1={}, Val2={", ia0).getFormattedMessage();
        assertEquals("Val1=1, Val2={", result);
    }

    @Test public void testArrayThrowable() {
        MessageFormatter.FormattingTuple ft;
        Throwable t = new Throwable();
        Object[] ia = new Object[]{i1, i2, i3, t};
        Object[] iaWitness = new Object[]{i1, i2, i3};

        ft = MessageFormatter
                .arrayFormat("Value {} is smaller than {} and {}.", ia);
        assertEquals("Value 1 is smaller than 2 and 3.", ft.getFormattedMessage());
        assertTrue(Arrays.equals(iaWitness, ft.getArgArray()));
        assertEquals(t, ft.getThrowable());

        ft = MessageFormatter.arrayFormat("{}{}{}", ia);
        assertEquals("123", ft.getFormattedMessage());
        assertTrue(Arrays.equals(iaWitness, ft.getArgArray()));
        assertEquals(t, ft.getThrowable());

        ft = MessageFormatter.arrayFormat("Value {} is smaller than {}.", ia);
        assertEquals("Value 1 is smaller than 2.", ft.getFormattedMessage());
        assertTrue(Arrays.equals(iaWitness, ft.getArgArray()));
        assertEquals(t, ft.getThrowable());

        ft = MessageFormatter.arrayFormat("Value {} is smaller than {}", ia);
        assertEquals("Value 1 is smaller than 2", ft.getFormattedMessage());
        assertTrue(Arrays.equals(iaWitness, ft.getArgArray()));
        assertEquals(t, ft.getThrowable());

        ft = MessageFormatter.arrayFormat("Val={}, {, Val={}", ia);
        assertEquals("Val=1, {, Val=2", ft.getFormattedMessage());
        assertTrue(Arrays.equals(iaWitness, ft.getArgArray()));
        assertEquals(t, ft.getThrowable());

        ft = MessageFormatter.arrayFormat("Val={}, \\{, Val={}", ia);
        assertEquals("Val=1, \\{, Val=2", ft.getFormattedMessage());
        assertTrue(Arrays.equals(iaWitness, ft.getArgArray()));
        assertEquals(t, ft.getThrowable());

        ft = MessageFormatter.arrayFormat("Val1={}, Val2={", ia);
        assertEquals("Val1=1, Val2={", ft.getFormattedMessage());
        assertTrue(Arrays.equals(iaWitness, ft.getArgArray()));
        assertEquals(t, ft.getThrowable());

        ft = MessageFormatter.arrayFormat(
                "Value {} is smaller than {} and {} -- {} .", ia);
        assertEquals("Value 1 is smaller than 2 and 3 -- " + t.toString() + " .",
                ft.getFormattedMessage());
        assertTrue(Arrays.equals(ia, ft.getArgArray()));
        assertNull(ft.getThrowable());

        ft = MessageFormatter.arrayFormat("{}{}{}{}", ia);
        assertEquals("123" + t.toString(), ft.getFormattedMessage());
        assertTrue(Arrays.equals(ia, ft.getArgArray()));
        assertNull(ft.getThrowable());
    }
}
