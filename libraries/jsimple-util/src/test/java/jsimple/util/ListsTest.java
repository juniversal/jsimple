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
public class ListsTest extends UnitTest {
	@Test public void testListEquals(){
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(1);
		a.add(2);
		a.add(3);
		
		ArrayList<Integer> b = new ArrayList<Integer>();
		b.add(1);
		b.add(2);
		b.add(3);
		
		boolean listsEqual = Lists.arrayListsEqual(a, b);
		assertTrue(listsEqual);
		
	}
	
	@Test public void testListNegativeEquals(){
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(3);
		a.add(1);
		a.add(2);
		
		
		ArrayList<Integer> b = new ArrayList<Integer>();
		b.add(1);
		b.add(2);
		b.add(3);
		
		boolean listsEqual = Lists.arrayListsEqual(a, b);
		assertFalse(listsEqual);
	}
	
	@Test public void testListDistinctElement(){
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(3);
		a.add(2);
		a.add(2);
		a.add(4);

		List<Integer> output = Lists.distinctElements(a);
		
		assertEquals(3, output.size());
		assertEquals(3, (int) output.get(0));
		assertEquals(2, (int) output.get(1));
		assertEquals(4, (int) output.get(2));
	}
}
