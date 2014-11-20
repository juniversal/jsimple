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
 * This code was adapted from Apache Harmony (http://http://harmony.apache.org).
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

/*
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.WeakHashMap;
*/

import jsimple.unit.UnitTest;
import org.junit.Test;

public class AbstractMapTest extends UnitTest {
    static final String specialKey = "specialKey".intern();

    static final String specialValue = "specialValue".intern();

    /**
     * @tests java.util.AbstractMap#keySet()
     */
    @Test public void test_keySet() {
        AbstractMap map1 = new HashMap(0);
        assertSame("HashMap(0)", map1.keySet(), map1.keySet());

        AbstractMap map2 = new HashMap(10);
        assertSame("HashMap(10)", map2.keySet(), map2.keySet());
    }

    /**
     * @tests java.util.AbstractMap#clear()
     */
    @Test public void test_clear() {
        // normal clear()
        AbstractMap map = new HashMap();
        map.put(1, 1);
        map.clear();
        assertTrue(map.isEmpty());
    }

    /**
     * @tests java.util.AbstractMap#values()
     */
    @Test public void test_values() {
        AbstractMap map1 = new HashMap(0);
        assertSame("HashMap(0)", map1.values(), map1.values());

        AbstractMap map2 = new HashMap(10);
        assertSame("HashMap(10)", map2.values(), map2.values());
    }

    /**
     * @tests java.util.AbstractMap#clone()
     */
    @Test public void test_clone() {
        class MyMap extends AbstractMap implements Cloneable {
            private Map map = new HashMap();

            public Set entrySet() {
                return map.entrySet();
            }

            public Object put(Object key, Object value) {
                return map.put(key, value);
            }

            public Map getMap() {
                return map;
            }

            public Object clone() {
                try {
                    return super.clone();
                } catch (CloneNotSupportedException e) {
                    return null;
                }
            }

            @Override public void clear() {
                map.clear();
            }

            @Override public boolean containsKey(Object key) {
                return map.containsKey(key);
            }

            @Override public boolean containsValue(Object value) {
                return map.containsValue(value);
            }

            @Override public Object get(Object key) {
                return map.get(key);
            }

            @Override public boolean isEmpty() {
                return map.isEmpty();
            }

            @Override public Set keySet() {
                return map.keySet();
            }

            @Override public void putAll(Map map) {
                this.map.putAll(map);
            }

            @Override public Object remove(Object key) {
                return map.remove(key);
            }

            @Override public int size() {
                return map.size();
            }

            @Override public Collection values() {
                return map.values();
            }
        }

        MyMap map = new MyMap();
        map.put("one", "1");
        Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();
        assertTrue("entry not added", entry.getKey() == "one"
                && entry.getValue() == "1");
        MyMap mapClone = (MyMap) map.clone();
        assertTrue("clone not shallow", map.getMap() == mapClone.getMap());
    }

    /**
     * @tests {@link jsimple.util.AbstractMap#putAll(Map)} and equals
     */
    @Test public void test_putAllLMap() {
        HashMap hm1 = new HashMap();
        HashMap hm2 = new HashMap();

        hm2.put("this", "that");
        hm1.putAll(hm2);
        assertEquals("Should be equal", hm1, hm2);
    }

    @Test public void testEqualsWithNullValues() {
        Map<String, String> a = new HashMap<String, String>();
        a.put("a", null);
        a.put("b", null);

        Map<String, String> b = new HashMap<String, String>();
        a.put("c", "cat");
        a.put("d", "dog");

        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test public void testNullsOnViews() {
        Map<String, String> nullHostile = new HashMap<String, String>();

        nullHostile.put("a", "apple");
        testNullsOnView(nullHostile.entrySet());

        nullHostile.put("a", "apple");
        testNullsOnView(nullHostile.keySet());

        nullHostile.put("a", "apple");
        testNullsOnView(nullHostile.values());
    }

    private void testNullsOnView(Collection<?> view) {
        assertFalse(view.contains(null));

        assertFalse(view.remove(null));

        HashSet setOfNull = new HashSet();
        setOfNull.add(null);
        assertFalse(view.equals(setOfNull));

        assertFalse(view.removeAll(setOfNull));

        assertTrue(view.retainAll(setOfNull)); // destructive
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }
}
