/*
 * Copyright (c) 2012-2015 Microsoft Mobile.  All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * This file is based on or incorporates material from Apache Harmony
 * http://harmony.apache.org (collectively, "Third Party Code"). Microsoft Mobile
 * is not the original author of the Third Party Code. The original copyright
 * notice and the license, under which Microsoft Mobile received such Third Party
 * Code, are set forth below.
 *
 *
 * Copyright 2006, 2010 The Apache Software Foundation.
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

public class ListTest extends UnitTest {

    static class SimpleList<T> extends List<T> {
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
        public int indexOf(T object) {
            return this.arrayList.indexOf(object);
        }

        @Override
        public int lastIndexOf(T object) {
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
        list.add(3);
        list.add(15);
        list.add(5);
        list.add(1);
        list.add(7);
        int hashCodeValue = 1;
        Iterator<Integer> i = list.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            hashCodeValue = 31 * hashCodeValue + (obj == null ? 0 : obj.hashCode());
        }
        assertTrue("Incorrect hashCode returned.  Wanted: " + hashCodeValue + " got: " + list.hashCode(), hashCodeValue == list.hashCode());
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

    static class MockArrayList<E> extends List<E> {
        ArrayList<E> list = new ArrayList<E>();

        public E remove(int idx) {
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
            list.add(idx, o);
        }

        @Override
        public Iterator<E> iterator() {
            return list.iterator();
        }

        @Override
        public int indexOf(E object) {
            return list.indexOf(object);
        }

        @Override
        public int lastIndexOf(E object) {
            return list.lastIndexOf(object);
        }

        @Override
        public E set(int location, E object) {
            return list.set(location, object);
        }
    }

    @Test
    public void testIndexOf() {
        List<String> list = new MockArrayList<String>();
        list.addAll(new ArrayList<String>("1", "2", "3", "4", "5"));

        assertEquals("find 0 in the list do not contain 0", -1, list.indexOf("0"));
        assertEquals("did not return the right location of element 3", 2, list.indexOf("3"));
        assertEquals("find null in the list do not contain null element", -1, list.indexOf(null));
        list.add(null);
        assertEquals("did not return the right location of element null", 5, list.indexOf(null));
    }

    @Test
    public void testLastIndexOf() {
        List<String> list = new MockArrayList<String>();
        list.addAll(new ArrayList<String>("1", "2", "3", "4", "5", "5", "4", "3", "2", "1"));

        assertEquals("find 6 in the list do not contain 6", -1, list.lastIndexOf("6"));
        assertEquals("did not return the right location of element 4", 6, list.lastIndexOf("4"));
        assertEquals("find null in the list do not contain null element", -1, list.lastIndexOf(null));
        list.add(null);
        assertEquals("did not return the right location of element null", 10, list.lastIndexOf(null));
    }
}
