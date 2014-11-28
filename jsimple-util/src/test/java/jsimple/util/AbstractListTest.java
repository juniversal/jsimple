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
import org.junit.Test;

import java.util.Iterator;

public class AbstractListTest extends UnitTest {

	static class SimpleList<T> extends AbstractList<T> {
		ArrayList<T> arrayList;

		SimpleList() {
			this.arrayList = new ArrayList<T>();
		}

		@Override
		public T get(int index) {
			return this.arrayList.get(index);
		}

		@Override
		public void add(int i, T o) {
			this.arrayList.add(i, o);
		}

		@Override
		public T remove(int i) {
			return this.arrayList.remove(i);
		}

		@Override
		public int size() {
			return this.arrayList.size();
		}

		@Override
		public Iterator<T> iterator() {
			return this.arrayList.iterator();
		}

		@Override
		public int indexOf(Object object) {
			return this.arrayList.indexOf(object);
		}

		@Override
		public int lastIndexOf(Object object) {
			return this.arrayList.lastIndexOf(object);
		}

		@Override
		public T set(int location, T object) {
			return this.arrayList.set(location, object);
		}
	}

	@Test
	public void testHashCode() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(3));
		list.add(new Integer(15));
		list.add(new Integer(5));
		list.add(new Integer(1));
		list.add(new Integer(7));
		int hashCode = 1;
		Iterator<Integer> i = list.iterator();
		while (i.hasNext()) {
			Object obj = i.next();
			hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
		}
		assertTrue("Incorrect hashCode returned.  Wanted: " + hashCode + " got: " + list.hashCode(), hashCode == list.hashCode());
	}

	@Test
	public void testIterator() {
		SimpleList<Object> list = new SimpleList<Object>();
		list.add(new Object());
		list.add(new Object());
		Iterator<Object> it = list.iterator();
		it.next();
		it.remove();
		it.next();
	}

	class MockArrayList<E> extends AbstractList<E> {
		ArrayList<E> list = new ArrayList<E>();

		public E remove(int idx) {
			modCount++;
			return list.remove(idx);
		}

		@Override
		public E get(int index) {
			return list.get(index);
		}

		@Override
		public int size() {
			return list.size();
		}

		@Override
		public void add(int idx, E o) {
			modCount += 10;
			list.add(idx, o);
		}

		@Override
		public Iterator<E> iterator() {
			return list.iterator();
		}

		@Override
		public int indexOf(Object object) {
			return list.indexOf(object);
		}

		@Override
		public int lastIndexOf(Object object) {
			return list.lastIndexOf(object);
		}

		@Override
		public E set(int location, E object) {
			return list.set(location, object);
		}
	}

	@Test
	public void testIndexOf() {
		AbstractList<Integer> list = new MockArrayList<Integer>();
		list.addAll(ArrayList.create(1, 2, 3, 4, 5));

		assertEquals("find 0 in the list do not contain 0", -1, list.indexOf(new Integer(0)));
		assertEquals("did not return the right location of element 3", 2, list.indexOf(new Integer(3)));
		assertEquals("find null in the list do not contain null element", -1, list.indexOf(null));
		list.add(null);
		assertEquals("did not return the right location of element null", 5, list.indexOf(null));
	}

	@Test
	public void testLastIndexOf() {
		AbstractList<Integer> list = new MockArrayList<Integer>();
		list.addAll(ArrayList.create(1, 2, 3, 4, 5, 5, 4, 3, 2, 1));

		assertEquals("find 6 in the list do not contain 6", -1, list.lastIndexOf(new Integer(6)));
		assertEquals("did not return the right location of element 4", 6, list.lastIndexOf(new Integer(4)));
		assertEquals("find null in the list do not contain null element", -1, list.lastIndexOf(null));
		list.add(null);
		assertEquals("did not return the right location of element null", 10, list.lastIndexOf(null));
	}
}
