/*
 * Copyright (c) 2012-2014, Microsoft Mobile
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
 *
 *
 * This code was adapted from Apache Harmony (http://harmony.apache.org).
 * The original Apache Harmony copyright is below.
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package jsimple.util;

import jsimple.unit.UnitTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

public class HashSetTest extends UnitTest {
	private HashSet<Object> hs;
	static Object[] objArray;

	static {
		objArray = new Object[1000];
		for (int i = 0; i < objArray.length; i++)
			objArray[i] = new Integer(i);
	}

	@Test
	public void testConstructor() {
		HashSet<Object> hs2 = new HashSet<Object>();
		assertEquals("Created incorrect HashSet", 0, hs2.size());
	}

	@Test
	public void testConstructorSize() {
		// Test for method java.util.HashSet(int)
		HashSet<Object> hs2 = new HashSet<Object>(5);
		assertEquals("Created incorrect HashSet", 0, hs2.size());
		try {
			new HashSet<Object>(-1);
			fail("Failed to throw IllegalArgumentException for capacity < 0");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testConstructorLoadFactor() {
		// Test for method java.util.HashSet(int, float)
		HashSet<Object> hs2 = new HashSet<Object>(5, (float) 0.5);
		assertEquals("Created incorrect HashSet", 0, hs2.size());
		try {
			new HashSet<Object>(0, 0);
			fail("Failed to throw IllegalArgumentException for initial load factor <= 0");
		} catch (IllegalArgumentException e) {
			// expected
		}

	}

	@Test
	public void testConstructorFromCollection() {
		// Test for method java.util.HashSet(java.util.Collection)
		HashSet<Object> hs2 = HashSet.create(objArray);

		for (Object anObjArray : objArray) {
			assertTrue("HashSet does not contain correct elements", hs.contains(anObjArray));
		}
		assertTrue("HashSet created from collection incorrect size", hs2.size() == objArray.length);
	}

	@Test
	public void testAdd() {
		// Test for method boolean java.util.HashSet.add(java.lang.Object)
		int size = hs.size();
		hs.add(new Integer(8));

		assertTrue("Added element already contained by set", hs.size() == size);
		hs.add(new Integer(-9));

		assertTrue("Failed to increment set size after add", hs.size() == size + 1);
		assertTrue("Failed to add element to set", hs.contains(new Integer(-9)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testClear() {
		// Test for method void java.util.HashSet.clear()
		Set<Object> orgSet = (Set<Object>) hs.clone();
		hs.clear();
		Iterator<Object> i = orgSet.iterator();
		assertEquals("Returned non-zero size after clear", 0, hs.size());

		while (i.hasNext()) {
			assertTrue("Failed to clear set", !hs.contains(i.next()));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testClone() {
		// Test for method java.lang.Object java.util.HashSet.clone()
		HashSet<Object> hs2 = (HashSet<Object>) hs.clone();
		
		assertTrue("clone returned an equivalent HashSet", hs != hs2);
		assertTrue("clone did not return an equal HashSet", hs.equals(hs2));
	}

	@Test
	public void testContainsNull() {
		// Test for method boolean java.util.HashSet.contains(java.lang.Object)
		assertTrue("Returned false for valid object", hs.contains(objArray[90]));
		assertTrue("Returned true for invalid Object", !hs.contains(new Object()));

		HashSet<Object> s = new HashSet<Object>();
		s.add(null);
		assertTrue("Cannot handle null", s.contains(null));
	}

	@Test
	public void testIsEmpty() {
		// Test for method boolean java.util.HashSet.isEmpty()
		assertTrue("Empty set returned false", new HashSet<>().isEmpty());
		assertTrue("Non-empty set returned true", !hs.isEmpty());
	}

	@Test
	public void testIterator() {
		// Test for method java.util.Iterator java.util.HashSet.iterator()
		Iterator<Object> i = hs.iterator();
		int x = 0;
		
		while (i.hasNext()) {
			assertTrue("Failed to iterate over all elements", hs.contains(i.next()));
			++x;
		}
		assertTrue("Returned iteration of incorrect size", hs.size() == x);

		HashSet<Object> s = new HashSet<Object>();
		s.add(null);
		assertNull("Cannot handle null", s.iterator().next());
	}

	@Test
	public void testRemove() {
		// Test for method boolean java.util.HashSet.remove(java.lang.Object)
		int size = hs.size();
		hs.remove(new Integer(98));
		assertTrue("Failed to remove element", !hs.contains(new Integer(98)));
		assertTrue("Failed to decrement set size", hs.size() == size - 1);

		HashSet<Object> s = new HashSet<Object>();
		s.add(null);
		assertTrue("Cannot handle null", s.remove(null));
	}

	/**
	 * @tests java.util.HashSet#size()
	 */
	@Test
	public void testSize() {
		// Test for method int java.util.HashSet.size()
		assertTrue("Returned incorrect size", hs.size() == (objArray.length + 1));
		hs.clear();
		assertEquals("Cleared set returned non-zero size", 0, hs.size());
	}

	/**
	 * @tests java.util.AbstractCollection#toString()
	 */
	@Test
	public void testToString() {
		HashSet<Object> s = new HashSet<Object>();
		s.add(s);
		String result = s.toString();
		assertTrue("should contain self ref", result.indexOf("(this") > -1);
	}

	/**
	 * Sets up the fixture, for example, open a network connection. This method
	 * is called before a test is executed.
	 */
	@Before
	public void setUp() {
		hs = new HashSet<Object>();
		for (int i = 0; i < objArray.length; i++)
			hs.add(objArray[i]);
		hs.add(null);
	}

	/**
	 * Tears down the fixture, for example, close a network connection. This
	 * method is called after a test is executed.
	 */
	@After
	public void tearDown() {
	}
	
	@Test
	public void testContainDistictType() {
		HashSet<Object> s = new HashSet<Object>();
		String data = "teststr";
		s.add(data);
		
		Object q = data;
		assertTrue("Cannot handle null", s.contains(q));
	}
	
	@Test
	public void testAddAll() {
		HashSet<String> sut = new HashSet<String>();
		ArrayList<String> insertData = new ArrayList<String>();
		insertData.add("1");
		insertData.add("2");

		sut.add("one");
		sut.addAll(insertData);

		assertEquals(3, sut.size());
		assertTrue(sut.contains("one"));
		assertTrue(sut.contains("1"));
		assertTrue(sut.contains("2"));
	}
	
	@Test
	public void testContainsAll() {
		HashSet<String> sut = new HashSet<String>();
		sut.add("2");
		sut.add("1");

		// reverser order
		ArrayList<String> testData = new ArrayList<String>();
		testData.add("1");
		testData.add("2");

		assertTrue(sut.containsAll(testData));
	}
	
	@Test
	public void testRemoveNegative() {
		HashSet<String> sut = new HashSet<String>();
		sut.add("2");
		sut.add("1");

		sut.remove("5");

		assertEquals(2, sut.size());
	}
	
	@Test
	public void testDuplication() {
		HashSet<String> sut = new HashSet<String>();
		sut.add("1");
		sut.add("1");
		
		assertEquals(1, sut.size());
	}
	
	@Test
	public void testRetainAll() {
		HashSet<String> sut = new HashSet<String>();
		sut.add("2");
		sut.add("1");
		sut.add("3");

		// reverser order
		ArrayList<String> testData = new ArrayList<String>();
		testData.add("1");
		testData.add("2");

		sut.retainAll(testData);

		assertEquals(2, sut.size());
		assertTrue(sut.contains("1"));
		assertTrue(sut.contains("2"));
		assertFalse(sut.contains("3"));

	}
}
