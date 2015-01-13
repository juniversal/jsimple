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
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

public class CollectionTest extends UnitTest {
    @Test public void testAddObject() {
        Collection<Object> ac = new TestAddObjectCollection(this);

        try {
            ac.add(null);
        } catch (ProgrammerError e) {
        }
    }

    private static class TestAddObjectCollection extends TestCollection<Object> {
        public TestAddObjectCollection(UnitTest unitTest) {
            super(unitTest);
        }

        @Override public Iterator<Object> iterator() {
            fail("iterator should not get called");
            return null;
        }

        @Override public int size() {
            fail("size should not get called");
            return 0;
        }
    }

    @Test public void testAddAll() {
        final Collection<String> fixtures = new ArrayList<String>("0", "1", "2");
        Collection<String> ac = new TestAddAllCollection(this, fixtures);
        assertTrue(ac.addAll(fixtures));
    }

    private static class TestAddAllCollection extends TestCollection<String> {
        private final Collection<String> fixtures;

        public TestAddAllCollection(UnitTest unitTest, Collection<String> fixtures) {
            super(unitTest);
            this.fixtures = fixtures;
        }

        @Override public boolean add(String object) {
            assertTrue(fixtures.contains(object));
            return true;
        }

        @Override public Iterator<String> iterator() {
            fail("iterator should not get called");
            return null;
        }

        @Override public int size() {
            fail("size should not get called");
            return 0;
        }
    }

    @Test public void testContainsAll() {
        final Collection<String> fixtures = new ArrayList<String>("0", "1", "2");
        Collection<String> ac = new TestContainsAllCollection(this, fixtures);
        assertTrue(ac.containsAll(fixtures));
    }

    private static class TestContainsAllCollection extends TestCollection<String> {
        private final Collection<String> fixtures;

        public TestContainsAllCollection(UnitTest unitTest, Collection<String> fixtures) {
            super(unitTest);
            this.fixtures = fixtures;
        }

        @Override public boolean contains(String object) {
            assertTrue(fixtures.contains(object));
            return true;
        }

        @Override public Iterator<String> iterator() {
            fail("iterator should not get called");
            return null;
        }

        @Override public int size() {
            fail("size should not get called");
            return 0;
        }
    }

    @Test public void testIsEmpty() {
        final boolean[] sizeCalled = new boolean[1];
        Collection<Object> ac = new TestIsEmptyCollection(this, sizeCalled);
        assertTrue(ac.isEmpty());
        assertTrue(sizeCalled[0]);
    }

    private static class TestIsEmptyCollection extends TestCollection<Object> {
        private final boolean[] sizeCalled;

        public TestIsEmptyCollection(UnitTest unitTest, boolean[] sizeCalled) {
            super(unitTest);
            this.sizeCalled = sizeCalled;
        }

        @Override public Iterator<Object> iterator() {
            fail("iterator should not get called");
            return null;
        }

        @Override public int size() {
            sizeCalled[0] = true;
            return 0;
        }
    }

    @Test public void testRemoveAll() {
        final String[] removed = new String[3];
        Collection<String> ac = new TestRemoveAllCollection(this, removed);

        assertTrue(ac.removeAll(new ArrayList<String>("0", "1", "2")));
        for (String r : removed) {
            if (!"0".equals(r) && !"1".equals(r) && !"2".equals(r)) {
                fail("an unexpected element was removed");
            }
        }
    }


    private static class TestRemoveAllCollection extends TestCollection<String> {
        private final String[] removed;

        public TestRemoveAllCollection(UnitTest unitTest, String[] removed) {
            super(unitTest);
            this.removed = removed;
        }

        @Override public Iterator<String> iterator() {
            return new TestRemoveAllCollectionIterator(this);
        }

        @Override public int size() {
            fail("size should not get called");
            return 0;
        }

        private static class TestRemoveAllCollectionIterator extends Iterator<String> {
            private TestRemoveAllCollection outer;
            String[] values = new String[]{"0", "1", "2"};
            int index;

            public TestRemoveAllCollectionIterator(TestRemoveAllCollection outer) {
                this.outer = outer;
            }

            public boolean hasNext() {
                return index < values.length;
            }

            public String next() {
                return values[index++];
            }

            public void remove() {
                outer.removed[index - 1] = values[index - 1];
            }
        }
    }

    @Test public void testRetainAll() {
        final String[] removed = new String[1];
        Collection<String> ac = new TestRetainAllCollection(this, removed);
        assertTrue(ac.retainAll(new ArrayList<String>("1", "2")));
        assertEquals("0", removed[0]);
    }

    private static class TestRetainAllCollection extends TestCollection<String> {
        private final String[] removed;

        public TestRetainAllCollection(UnitTest unitTest, String[] removed) {
            super(unitTest);
            this.removed = removed;
        }

        @Override public Iterator<String> iterator() {
            return new TestRetainAllCollectionIterator(this);
        }

        @Override public int size() {
            fail("size should not get called");
            return 0;
        }

        private static class TestRetainAllCollectionIterator extends Iterator<String> {
            private TestRetainAllCollection outer;
            String[] values = new String[]{"0", "1", "2"};
            int index;

            public TestRetainAllCollectionIterator(TestRetainAllCollection outer) {
                this.outer = outer;
            }

            public boolean hasNext() {
                return index < values.length;
            }

            public String next() {
                return values[index++];
            }

            public void remove() {
                outer.removed[index - 1] = values[index - 1];
            }
        }
    }

    @Test public void testToArrayTypeless() {
        Collection<String> ac = new TestToArrayTypelessCollection(this);

        Object[] array = ac.toArray();
        assertEquals(3, array.length);
        for (Object o : array) {
            if (!"0".equals(o) && !"1".equals(o) && !"2".equals(o)) {
                fail("an unexpected element was removed");
            }
        }
    }


    private static class TestToArrayTypelessCollection extends TestCollection<String> {
        public TestToArrayTypelessCollection(UnitTest unitTest) {
            super(unitTest);
        }

        @Override public Iterator<String> iterator() {
            return new TestToArrayTypelessCollectionIterator(this);
        }

        @Override public int size() {
            return 3;
        }

        private static class TestToArrayTypelessCollectionIterator extends Iterator<String> {
            private TestToArrayTypelessCollection outer;
            String[] values = new String[]{"0", "1", "2"};
            int index;

            public TestToArrayTypelessCollectionIterator(TestToArrayTypelessCollection outer) {
                this.outer = outer;
            }

            public boolean hasNext() {
                return index < values.length;
            }

            public String next() {
                return values[index++];
            }

            public void remove() {
                outer.fail("remove should not get called");
            }
        }
    }

    @Test public void testToArrayTyped() {
        Collection<String> ac = new TestToArrayTypedCollection(this);

        String[] csa = new String[3];
        ac.toArray(csa);
        assertEquals(3, csa.length);
        assertEquals("0", csa[0]);
        assertEquals("1", csa[1]);
        assertEquals("2", csa[2]);
    }

    private static class TestToArrayTypedCollection extends TestCollection<String> {
        public TestToArrayTypedCollection(UnitTest unitTest) {
            super(unitTest);
        }

        @Override public Iterator<String> iterator() {
            return new TestToArrayTypedCollectionIterator(this);
        }

        @Override public int size() {
            return 3;
        }

        private static class TestToArrayTypedCollectionIterator extends Iterator<String> {
            private TestToArrayTypedCollection outer;
            String[] values = new String[]{"0", "1", "2"};
            int index;

            public TestToArrayTypedCollectionIterator(TestToArrayTypedCollection outer) {
                this.outer = outer;
            }

            public boolean hasNext() {
                return index < values.length;
            }

            public String next() {
                return values[index++];
            }

            public void remove() {
                outer.fail("remove should not get called");
            }
        }
    }

    private static abstract class TestCollection<T> extends Collection<T> {
        private UnitTest unitTest;

        public TestCollection(UnitTest unitTest) {
            this.unitTest = unitTest;
        }

        public void fail(@Nullable String message) {
            unitTest.fail(message);
        }

        public void assertTrue(boolean condition) { unitTest.assertTrue(condition); }
    }
}
