/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from Apache Harmony
 * http://harmony.apache.org (collectively, “Third Party Code”). Microsoft Mobile
 * is not the original author of the Third Party Code. The original copyright
 * notice and the license, under which Microsoft Mobile received such Third Party
 * Code, are set forth below.
 *
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jsimple.util;

import jsimple.unit.UnitTest;
import org.junit.Test;

public class AbstractListTest extends UnitTest {

    static  class SimpleList extends List {
        ArrayList arrayList;

        SimpleList() {
            this.arrayList = new ArrayList();
        }

        @Override public Object get(int index) {
            return this.arrayList.get(index);
        }

        @Override public void add(int i, Object o) {
            this.arrayList.add(i, o);
        }

        @Override public Object remove(int i) {
            return this.arrayList.remove(i);
        }

        @Override public int size() {
            return this.arrayList.size();
        }

        @Override public Iterator iterator() {
            return this.arrayList.iterator();
        }

        @Override public int indexOf(Object object) {
            return this.arrayList.indexOf(object);
        }

        @Override public int lastIndexOf(Object object) {
            return this.arrayList.lastIndexOf(object);
        }

        @Override public Object set(int location, Object object) {
            return this.arrayList.set(location, object);
        }
    }

    /**
     * @tests java.util.AbstractList#hashCode()
     */
    @Test public void test_hashCode() {
        List list = new ArrayList();
        list.add(new Integer(3));
        list.add(new Integer(15));
        list.add(new Integer(5));
        list.add(new Integer(1));
        list.add(new Integer(7));
        int hashCode = 1;
        Iterator i = list.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }
        assertTrue("Incorrect hashCode returned.  Wanted: " + hashCode
                + " got: " + list.hashCode(), hashCode == list.hashCode());
    }

    /**
     * @tests java.util.AbstractList#iterator()
     */
    @Test public void test_iterator() {
        SimpleList list = new SimpleList();
        list.add(new Object());
        list.add(new Object());
        Iterator it = list.iterator();
        it.next();
        it.remove();
        it.next();
    }

    class MockArrayList<E> extends List<E> {
        /**
         * A counter for changes to the list.
         */
        protected transient int modCount;

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        ArrayList<E> list = new ArrayList<E>();

        public E remove(int idx) {
            modCount++;
            return list.remove(idx);
        }

        @Override public E get(int index) {
            return list.get(index);
        }

        @Override public int size() {
            return list.size();
        }

        @Override public void add(int idx, E o) {
            modCount += 10;
            list.add(idx, o);
        }

        @Override public Iterator<E> iterator() {
            return list.iterator();
        }

        @Override public int indexOf(Object object) {
            return list.indexOf(object);
        }

        @Override public int lastIndexOf(Object object) {
            return list.lastIndexOf(object);
        }

        @Override public E set(int location, E object) {
            return list.set(location, object);
        }
    }

    /**
     * @tests {@link java.util.AbstractList#indexOf(Object)}
     */
    @Test public void test_indexOf_Ljava_lang_Object() {
        List<Integer> list = new MockArrayList<Integer>();
        list.addAll(ArrayList.create(1, 2, 3, 4, 5));

        assertEquals("find 0 in the list do not contain 0", -1, list.indexOf(new Integer(0)));
        assertEquals("did not return the right location of element 3", 2, list.indexOf(new Integer(3)));
        assertEquals("find null in the list do not contain null element", -1, list.indexOf(null));
        list.add(null);
        assertEquals("did not return the right location of element null", 5, list.indexOf(null));
    }

    /**
     * @add tests {@link java.util.AbstractList#lastIndexOf(Object)}
     */
    @Test public void test_lastIndexOf_Ljava_lang_Object() {
        List<Integer> list = new MockArrayList<Integer>();
        list.addAll(ArrayList.create(1, 2, 3, 4, 5, 5, 4, 3, 2, 1));

        assertEquals("find 6 in the list do not contain 6", -1, list.lastIndexOf(new Integer(6)));
        assertEquals("did not return the right location of element 4", 6, list.lastIndexOf(new Integer(4)));
        assertEquals("find null in the list do not contain null element", -1, list.lastIndexOf(null));
        list.add(null);
        assertEquals("did not return the right location of element null", 10,list.lastIndexOf(null));
    }
}
