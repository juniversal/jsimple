/*
 * Copyright (c) 2012-2015, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jsimple.util;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Dorin Suletea
 * @since 12/1/2014
 */
public class StringUtilsTest extends UnitTest {
	@Test public void testSplit() {
		testSplit(new String[] {"a", "b", "c" }, StringUtils.split("a,b,c", ','));
		testSplit(new String[] {"a", "b,c" }, StringUtils.split("a,b,c", ',', 2));
		testSplit(new String[] {"a,b,c" }, StringUtils.split("a,b,c", ',', 1));
		testSplit(new String[] {"a,b,c" }, StringUtils.split("a,b,c", '-', 0));
	}

	public void testSplit(String[] expected, List<String> result) {
		String[] resultArray = new String[result.size()];
		result.toArray(resultArray);

		assertArrayEquals(expected, resultArray);
	}
}
