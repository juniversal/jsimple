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

public final class HashMapTest extends UnitTest {
    private HashMap<Object, Object> hm;
    private final static int hmSize = 1000;
    private static Object[] objArray;
    private static Object[] objArray2;

    @Override public void setUp() {
        objArray = new Object[hmSize];
        objArray2 = new Object[hmSize];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = i;
            objArray2[i] = objArray[i].toString();
        }

        hm = new HashMap<Object, Object>();
        for (int i = 0; i < objArray.length; i++) {
            hm.put(objArray2[i], objArray[i]);
        }
        hm.put("test", null);
    }

    @Test
    public void test_Constructor() {
        // Test for method java.util.HashMap()
        // TODO: Find source for this
        /*
		 * new Support_MapTest2(new HashMap()).runTest();
		 */

        HashMap<Object, Object> hm2 = new HashMap<Object, Object>();
        assertEquals("Created incorrect HashMap", 0, hm2.size());
    }

    @Test
    public void testConstructorI() {
        // Test for method java.util.HashMap(int)
        HashMap<Object, Object> hm2 = new HashMap<Object, Object>(5);
        assertEquals("Created incorrect HashMap", 0, hm2.size());

        try {
            new HashMap<Object, Object>(-1);
            fail("Failed to throw IllegalArgumentException for initial capacity < 0");
        } catch (ProgrammerError e) {
            // expected
        }

        HashMap<Object, Object> empty = new HashMap<Object, Object>(0);
        assertNull("Empty hashmap access", empty.get("nothing"));

        empty.put("something", "here");
        assertTrue("cannot get element", empty.get("something") == "here");
    }

    @Test
    public void testConstructorIF() {
        // Test for method java.util.HashMap(int, float)
        HashMap<Object, Object> hm2 = new HashMap<Object, Object>(5, (float) 0.5);
        assertEquals("Created incorrect HashMap", 0, hm2.size());
        try {
            new HashMap<Object, Object>(0, 0);
            fail("Failed to throw IllegalArgumentException for initial load factor <= 0");
        } catch (ProgrammerError e) {
            // expected
        }

        HashMap<Object, Object> empty = new HashMap<Object, Object>(0, 0.75f);
        assertNull("Empty hashtable access", empty.get("nothing"));

        empty.put("something", "here");
        assertTrue("cannot get element", empty.get("something") == "here");
    }

    @Test
    public void testConstructorMap() {
        // Test for method java.util.HashMap(java.util.Map)
        Map<Object, Object> myMap = new HashMap<Object, Object>();
        for (int counter = 0; counter < hmSize; counter++) {
            myMap.put(objArray2[counter], objArray[counter]);
        }

        HashMap<Object, Object> hm2 = new HashMap<Object, Object>(myMap);
        for (int counter = 0; counter < hmSize; counter++) {
            assertTrue("Failed to construct correct HashMap", hm.get(objArray2[counter]) == hm2.get(objArray2[counter]));
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("a", "a");
        SubMap<String, String> map2 = new SubMap<String, String>(map);

        assertTrue(map2.containsKey("a"));
        assertTrue(map2.containsValue("a"));
    }

    @Test
    public void testClear() {
        hm.clear();
        assertEquals("Clear failed to reset size", 0, hm.size());
        for (int i = 0; i < hmSize; i++) {
            assertNull("Failed to clear all elements", hm.get(objArray2[i]));
        }

        HashMap<Integer, String> map = new HashMap<Integer, String>();
        for (int i = -32767; i < 32768; i++) {
            map.put(i, "foobar");
        }

        map.clear();
        assertEquals("Failed to reset size on large integer map", 0, hm.size());
        for (int i = -32767; i < 32768; i++) {
            assertNull("Failed to clear integer map values", map.get(i));
        }
    }

    @Test
    public void testClone() {
        // Test for method java.lang.Object java.util.HashMap.clone()
        HashMap<Object, Object> hm2 = new HashMap<Object, Object>(hm);
        assertTrue("Clone answered equivalent HashMap", hm2 != hm);

        for (int counter = 0; counter < hmSize; counter++) {
            assertTrue("Clone answered unequal HashMap", hm.get(objArray2[counter]) == hm2.get(objArray2[counter]));
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("key", "value");
        // get the keySet() and values() on the original Map
        Set<String> keys = map.keySet();
        Collection<String> values = map.values();
        assertEquals("values() does not work", "value", values.iterator().next());
        assertEquals("keySet() does not work", "key", keys.iterator().next());

        Map<String, String> map2 = new HashMap<String, String>(map);
        map2.put("key", "value2");
        Collection<String> values2 = map2.values();
        assertTrue("values() is identical", values2 != values);
        // values() and keySet() on the cloned() map should be different
        assertEquals("values() was not cloned", "value2", values2.iterator().next());

        map2.clear();
        map2.put("key2", "value3");

        Set<String> key2 = map2.keySet();
        assertTrue("keySet() is identical", key2 != keys);
        assertEquals("keySet() was not cloned", "key2", key2.iterator().next());

        // regresion test for HARMONY-4603
        HashMap<Integer, MockClonable> hashmap = new HashMap<Integer, MockClonable>();
        MockClonable mock = new MockClonable(1);
        hashmap.put(1, mock);
        assertEquals(1, hashmap.get(1).i);
        HashMap<Integer, MockClonable> hm3 = new HashMap<Integer, MockClonable>(hashmap);
        assertEquals(1, hm3.get(1).i);
        mock.i = 0;
        assertEquals(0, hashmap.get(1).i);
        assertEquals(0, hm3.get(1).i);
    }

    @Test
    public void testContainsKey() {
        // Test for method boolean
        // java.util.HashMap.containsKey(java.lang.Object)
        assertTrue("Returned false for valid key", hm.containsKey(Integers.toString(876)));
        assertTrue("Returned true for invalid key", !hm.containsKey("KKDKDKD"));
    }

    @Test
    public void testContainsValue() {
        // Test for method boolean
        // java.util.HashMap.containsValue(java.lang.Object)
        assertTrue("Returned false for valid value", hm.containsValue(875));
        assertTrue("Returned true for invalid valie", !hm.containsValue(-9));
    }

    @Test
    public void testEntrySet() {
        // Test for method java.util.Set java.util.HashMap.entrySet()
        Set<MapEntry<Object, Object>> s = hm.entrySet();
        Iterator<MapEntry<Object, Object>> i = s.iterator();
        assertTrue("Returned set of incorrect size", hm.size() == s.size());

        while (i.hasNext()) {
            MapEntry<Object, Object> m = (MapEntry<Object, Object>) i.next();
            assertTrue("Returned incorrect entry set", hm.containsKey(m.getKey()) && hm.containsValue(m.getValue()));
        }

        Iterator<MapEntry<Object, Object>> iter = s.iterator();
        s.remove(iter.next());
        assertEquals(1000, s.size());
    }

    @Test
    public void testGet() {
        // Test for method java.lang.Object
        // java.util.HashMap.get(java.lang.Object)
        assertNull("Get returned non-null for non existent key", hm.get("T"));
        hm.put("T", "HELLO");
        assertEquals("Get returned incorrect value for existing key", "HELLO", hm.get("T"));

        // Regression for HARMONY-206
        ReusableKey k = new ReusableKey();
        HashMap<ReusableKey, String> map = new HashMap<ReusableKey, String>();
        k.setKey(1);
        map.put(k, "value1");

        k.setKey(18);
        assertNull(map.get(k));

        k.setKey(17);
        assertNull(map.get(k));
    }

    @Test
    public void testIsEmpty() {
        // Test for method boolean java.util.HashMap.isEmpty()
        assertTrue("Returned false for new map", new HashMap<Object, Object>().isEmpty());
        assertTrue("Returned true for non-empty", !hm.isEmpty());
    }

    @Test
    public void testKeySet() {
        // Test for method java.util.Set java.util.HashMap.keySet()
        Set<Object> s = hm.keySet();
        assertTrue("Returned set of incorrect size()", s.size() == hm.size());
        for (int i = 0; i < objArray.length; i++) {
            assertTrue("Returned set does not contain all keys", s.contains(objArray[i].toString()));
        }

        Map<Integer, String> map = new HashMap<Integer, String>(101);
        map.put(1, "1");
        map.put(102, "102");
        map.put(203, "203");
        Iterator<Integer> it = map.keySet().iterator();
        Integer remove1 = it.next();
        it.hasNext();
        it.remove();
        Integer remove2 = it.next();
        it.remove();
        ArrayList<Integer> list = new ArrayList<Integer>(1, 102, 203);
        list.remove(remove1);
        list.remove(remove2);
        assertEquals(it.next(), list.get(0));
        assertEquals("Wrong size", 1, map.size());
        assertTrue("Wrong contents", map.keySet().iterator().next().equals(list.get(0)));

        Map<Integer, String> map2 = new HashMap<Integer, String>(101);
        map2.put(1, "1");
        map2.put(4, "4");
        Iterator<Integer> it2 = map2.keySet().iterator();
        int remove3 = it2.next();
        int next;
        if (remove3 == 1)
            next = 4;
        else
            next = 1;

        it2.hasNext();
        it2.remove();
        assertTrue("Wrong result 2", it2.next() == next);
        assertEquals("Wrong size 2", 1, map2.size());
        assertTrue("Wrong contents 2", map2.keySet().iterator().next() == next);
    }

    @Test
    public void testPut() {
        hm.put("KEY", "VALUE");
        assertEquals("Failed to install key/value pair", "VALUE", hm.get("KEY"));

        HashMap<Object, Object> m = new HashMap<Object, Object>();
        m.put(0L, "long");
        m.put(0, "int");
        assertEquals("Failed adding to bucket containing null", "long", m.get(0L));
        assertEquals("Failed adding to bucket containing null2", "int", m.get(0));

        // Check my actual key instance is returned
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        for (int i = -32767; i < 32768; i++) {
            map.put(i, "foobar");
        }
        int myKey = 0;
        // Put a new value at the old key position
        map.put(myKey, "myValue");
        assertTrue(map.containsKey(myKey));
        assertEquals("myValue", map.get(myKey));
/*        boolean found = false;
        for (Iterator<Integer> itr = map.keySet().iterator(); itr.hasNext(); ) {
            int key = itr.next();
            if (found = key == myKey) {
                break;
            }
        }
        assertFalse("Should not find new key instance in hashmap", found);*/

        // Add a new key instance and check it is returned
        assertNotNull(map.remove(myKey));
        map.put(myKey, "myValue");
        assertTrue(map.containsKey(myKey));
        assertEquals("myValue", map.get(myKey));
        boolean found = false;
        for (Iterator<Integer> itr = map.keySet().iterator(); itr.hasNext(); ) {
            Integer key = itr.next();
            if (found = key == myKey) {
                break;
            }
        }
        assertTrue("Did not find new key instance in hashmap", found);

        // Ensure keys with identical hashcode are stored separately
        HashMap<Object, Object> objmap = new HashMap<Object, Object>();
        for (int i = 0; i < 32768; i++) {
            objmap.put(i, "foobar");
        }
        // Put non-equal object with same hashcode
        MyKey aKey = new MyKey();
        assertNull(objmap.put(aKey, "value"));
        assertNull(objmap.remove(new MyKey()));
        assertEquals("foobar", objmap.get(0));
        assertEquals("value", objmap.get(aKey));
    }

    static class MyKey {
        public MyKey() {
            super();
        }

        public int hashCode() {
            return 0;
        }
    }

    /**
     * @tests java.util.HashMap#putAll(java.util.Map)
     */
    @Test
    public void testPutAll() {
        // Test for method void java.util.HashMap.putAll(java.util.Map)
        HashMap<Object, Object> hm2 = new HashMap<Object, Object>();
        hm2.putAll(hm);
        for (int i = 0; i < 1000; i++) {
            assertTrue("Failed to clear all elements", (Integer) hm2.get(Integers.toString(i)) == i);
        }

        HashMap<Object, Object> emptyHashMap = new HashMap<Object, Object>();
        hm2 = new HashMap<Object, Object>();
        hm2.putAll(emptyHashMap);
        assertEquals("Size should be 0", 0, hm2.size());
    }

    /**
     * @tests java.util.HashMap#remove(java.lang.Object)
     */
    @Test
    public void testRemove() {
        int size = hm.size();
        int y = 9;
        int x = (Integer) hm.remove(Integers.toString(y));
        assertTrue("Remove returned incorrect value", x == 9);
        assertNull("Failed to remove given key", hm.get(9));
        assertTrue("Failed to decrement size", hm.size() == (size - 1));
        assertNull("Remove of non-existent key returned non-null", hm.remove("LCLCLC"));

        HashMap<Integer, Object> map = new HashMap<Integer, Object>();
        for (int i = 0; i < 32768; i++) {
            map.put(i, "const");
        }
        Object[] values = new Object[32768];
        for (int i = 0; i < 32768; i++) {
            values[i] = new Object();
            map.put(i, values[i]);
        }
        for (int i = 32767; i >= 0; i--) {
            assertEquals("Failed to remove same value", values[i], map.remove(i));
        }

        // Ensure keys with identical hashcode are removed properly
        map = new HashMap<Integer, Object>();
        for (int i = -32767; i < 32768; i++) {
            map.put(i, "foobar");
        }
        // Remove non equal object with same hashcode
        // TOD: Add test for this
        //assertNull(map.remove(new MyKey()));
        assertEquals("foobar", map.get(0));
        map.remove(0);
        assertNull(map.get(0));
    }

    /**
     * Compatibility test to ensure we rehash the same way as the RI. Not required by the spec, but some apps seem
     * sensitive to it.
     */
    @Test
    public void testRehash() {
        // This map should rehash on adding the ninth element.
		HashMap<MyKey, Integer> hm = new HashMap<MyKey, Integer>(10, 0.5f);

        // Ordered set of keys.
        MyKey[] keyOrder = new MyKey[9];
        for (int i = 0; i < keyOrder.length; i++) {
            keyOrder[i] = new MyKey();
        }

        // Store eight elements
        for (int i = 0; i < 8; i++) {
			hm.put(keyOrder[i], i);
        }
        // Check expected ordering (inverse of adding order)
        MyKey[] returnedKeys = new MyKey[9];
        hm.keySet().toArray(returnedKeys);
        for (int i = 0; i < 8; i++) {
            assertSame(null, keyOrder[i], returnedKeys[7 - i]);
        }

        // The next put causes a rehash
		hm.put(keyOrder[8], 8);
        // Check expected new ordering (adding order)
        hm.keySet().toArray(returnedKeys);
        for (int i = 0; i < 9; i++) {
            assertSame(null, keyOrder[i], returnedKeys[i]);
        }
    }

    @Test public void testSize() {
        assertTrue("Returned incorrect size", hm.size() == (objArray.length + 1));
    }

    @Test
    public void testValues() {
        // Test for method java.util.Collection java.util.HashMap.values()
        Collection<Object> c = hm.values();
        assertTrue("Returned collection of incorrect size()", c.size() == hm.size());

        for (int i = 0; i < objArray.length; i++) {
            assertTrue("Returned collection does not contain all keys", c.contains(objArray[i]));
        }

        HashMap<Object, Object> myHashMap = new HashMap<Object, Object>();
        for (int i = 0; i < 100; i++) {
            myHashMap.put(objArray2[i], objArray[i]);
        }

        Collection<Object> values = myHashMap.values();
        // TODO: Find source for this
		/*
		 * new Support_UnmodifiableCollectionTest(
		 * "Test Returned Collection From HashMap.values()", values) .runTest();
		 */
		values.remove(0);
        assertTrue("Removing from the values collection should remove from the original map", !myHashMap.containsValue(0));
    }

/*
    @Test
    public void testToString() {
        HashMap<Object, Object> m = new HashMap<Object, Object>();
        m.put(m, m);
        String result = m.toString();
        assertTrue("should contain self ref", result.indexOf("(this") > -1);
    }
*/

    static class ReusableKey {
        private int key = 0;

        public void setKey(int key) {
            this.key = key;
        }

        public int hashCode() {
            return key;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ReusableKey)) {
                return false;
            }
            return key == ((ReusableKey) o).key;
        }
    }

    @Test
    public void testMapEntryHashCode() {
        // Related to HARMONY-403
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(10);
		int key = 1;
		int val = 2;
        map.put(key, val);
        int expected = ((Object) key).hashCode() ^ ((Object) val).hashCode();
        assertEquals(expected, map.hashCode());
        key = 4;
        val = 8;
        map.put(key, val);
        expected += ((Object) key).hashCode() ^ ((Object) val).hashCode();
        assertEquals(expected, map.hashCode());
    }

    // This class was named MockClonable in the Harmony source, but in it's current form isn't really cloneable anymore.
    // Though that's OK, as it doesn't actually need to be cloned to do the tests here
    static class MockClonable {
        public int i;

        public MockClonable(int i) {
            this.i = i;
        }

/*
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return new MockClonable(i);
        }
*/
    }

    /*
     * Regression test for HY-4750
     */
    @Test
    public void testEntryKeySet() {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "ONE");

		Set<MapEntry<Integer, String>> entrySet = map.entrySet();
		Iterator<MapEntry<Integer, String>> e = entrySet.iterator();

        Object real = e.next();
		MapEntry<Integer, String> copyEntry = new MockEntry();
        assertEqualTo((MapEntry<Integer, String>) real, copyEntry);
        assertTrue(entrySet.contains(copyEntry));

        entrySet.remove(copyEntry);
        assertFalse(entrySet.contains(copyEntry));
    }

    private static class MockEntry implements MapEntry<Integer, String> {
        @Override public boolean equalTo(@Nullable MapEntry<Integer, String> other) {
            if (other == null)
                return false;

            return other.getKey() == 1 && other.getValue().equals("ONE");
        }

        public Integer getKey() {
            return 1;
        }

        public String getValue() {
            return "ONE";
        }

        public String setValue(String value) {
            return null;
        }
    }

    static class SubMap<K, V> extends HashMap<K, V> {
        public SubMap(Map<K, V> m) {
            super(m);
        }

        public V put(K key, V value) {
            throw new ProgrammerError("Unsupported operation");
        }
    }

    @Test
    public void testReplaceKey() {
        HashMap<Integer, String> sut = new HashMap<Integer, String>();

        sut.put(1, "first");
        sut.put(1, "second");

        assertEquals(1, sut.keySet().size());
        assertEquals("second", sut.get(1));
    }

    @Test
    public void testGetValues() {
        HashMap<Integer, String> sut = new HashMap<Integer, String>();
        sut.put(1, "a");
        sut.put(2, "b");
        sut.put(3, "c");
        sut.put(4, "d");
        sut.put(5, "e");
        sut.put(6, "a");
        sut.values().toArray()[0] = "a";

        Collection<String> val = sut.values();
        assertEquals(val.size(), 6);

        // test if contains item from initial dataset, do not use indices since
        // the order is not fixed
        assertTrue(val.contains("a"));
        val.remove("a");
        // test if duplicated value allowed
        assertTrue(val.contains("a"));
    }
}
