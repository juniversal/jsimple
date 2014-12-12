/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
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

import org.jetbrains.annotations.Nullable;

/**
 * HashMap is an implementation of Map. All optional operations (adding and removing) are supported. Keys and values can
 * be any objects.
 * <p/>
 * Changes from the java.util version:
 * <p/>
 * Does not implement Serializable
 * <p/>
 * Does not support clone; use HashMap constructor taking a Map argument instead (which is more flexible and type safe)
 */
public class HashMap<K, V> extends Map<K, V> {
    // Lazily initialized key set.
    Set<K> keysSet;

    Collection<V> valuesCollection;

    /*
     * Actual count of entries
     */
    transient int elementCount;

    /*
     * The internal data structure to hold Entries
     */
    transient HashMapEntry<K, V>[] elementData;

    /*
     * modification count, to keep track of structural modifications between the
     * HashMap and the iterator
     */
    transient int modCount = 0;

    /*
     * default size that an HashMap created using the default constructor would
     * have.
     */
    private static final int DEFAULT_SIZE = 16;

    /*
     * maximum ratio of (stored elements)/(storage size) which does not lead to
     * rehash
     */
    final float loadFactor;

    /*
     * maximum number of elements that can be put in this map before having to
     * rehash
     */
    int threshold;

    private static abstract class AbstractMapIterator<K, V, E> extends Iterator<E> {
        private int position = 0;
        int expectedModCount;
        HashMapEntry<K, V> futureEntry;
        HashMapEntry<K, V> currentEntry;
        HashMapEntry<K, V> prevEntry;

        final HashMap<K, V> associatedMap;

        AbstractMapIterator(HashMap<K, V> hm) {
            associatedMap = hm;
            expectedModCount = hm.modCount;
            futureEntry = null;
        }

        @Override public boolean hasNext() {
            if (futureEntry != null) {
                return true;
            }
            while (position < associatedMap.elementData.length) {
                if (associatedMap.elementData[position] == null) {
                    position++;
                } else {
                    return true;
                }
            }
            return false;
        }

        final void checkConcurrentMod() {
            if (expectedModCount != associatedMap.modCount) {
                throw new ProgrammerError(
                        "HashMap modified outside of iterator: expected mod count is {}, actual mod count is {}",
                        expectedModCount, associatedMap.modCount);
            }
        }

        final void makeNext() {
            checkConcurrentMod();
            if (!hasNext()) {
                throw new ProgrammerError();
            }
            if (futureEntry == null) {
                currentEntry = associatedMap.elementData[position++];
                futureEntry = currentEntry.next;
                prevEntry = null;
            } else {
                if (currentEntry != null) {
                    prevEntry = currentEntry;
                }
                currentEntry = futureEntry;
                futureEntry = futureEntry.next;
            }
        }

        @Override public final void remove() {
            checkConcurrentMod();
            if (currentEntry == null) {
                throw new ProgrammerError("No current element to remove");
            }
            if (prevEntry == null) {
                int index = currentEntry.origKeyHash & (associatedMap.elementData.length - 1);
                associatedMap.elementData[index] = associatedMap.elementData[index].next;
            } else {
                prevEntry.next = currentEntry.next;
            }
            currentEntry = null;
            expectedModCount++;
            associatedMap.modCount++;
            associatedMap.elementCount--;
        }
    }

    private static class EntryIterator<K, V> extends AbstractMapIterator<K, V, MapEntry<K, V>> {
        EntryIterator(HashMap<K, V> map) {
            super(map);
        }

        @Override public MapEntry<K, V> next() {
            makeNext();
            return currentEntry;
        }
    }

    private static class KeyIterator<K, V> extends AbstractMapIterator<K, V, K> {
        KeyIterator(HashMap<K, V> map) {
            super(map);
        }

        @Override public K next() {
            makeNext();
            return currentEntry.key;
        }
    }

    private static class ValueIterator<K, V> extends AbstractMapIterator<K, V, V> {
        ValueIterator(HashMap<K, V> map) {
            super(map);
        }

        @Override public V next() {
            makeNext();
            return currentEntry.value;
        }
    }

    private static final class HashMapEntrySet<KT, VT> extends Set<MapEntry<KT, VT>> {
        private final HashMap<KT, VT> associatedMap;

        public HashMapEntrySet(HashMap<KT, VT> hm) {
            associatedMap = hm;
        }

        @Override public int size() {
            return associatedMap.elementCount;
        }

        @Override public void clear() {
            associatedMap.clear();
        }

        @Override public boolean add(MapEntry<KT, VT> object) {
            throw new ProgrammerError("add method not supported for HashMap entrySet");
        }

        @Override public boolean remove(MapEntry<KT, VT> object) {
            if (object != null) {
                HashMapEntry<KT, VT> entry = associatedMap.getEntry(object.getKey());
                if (valuesEq(entry, object)) {
                    associatedMap.removeEntry(entry);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean contains(MapEntry<KT, VT> object) {
            if (object == null)
                return false;
            HashMapEntry<KT, VT> entry = associatedMap.getEntry(object.getKey());
            return valuesEq(entry, object);
        }

        private static boolean valuesEq(HashMapEntry<KT, VT> entry, MapEntry<?, ?> oEntry) {
            return (entry != null) &&
                    ( (entry.value == null) ? (oEntry.getValue() == null) : (areEqualValues(entry.value, oEntry.getValue())) );
        }

        @Override
        public Iterator<MapEntry<KT, VT>> iterator() {
            return new EntryIterator<KT, VT>(associatedMap);
        }
    }

    /**
     * Create a new element array
     *
     * @param s
     * @return Reference to the element array
     */
    @SuppressWarnings("unchecked") HashMapEntry<K, V>[] newElementArray(int s) {
        return (HashMapEntry<K, V>[]) new Object[s];
    }

    /**
     * Constructs a new empty {@code HashMap} instance.
     */
    public HashMap() {
        this(DEFAULT_SIZE);
    }

    /**
     * Constructs a new {@code HashMap} instance with the specified capacity.
     *
     * @param capacity the initial capacity of this hash map.
     * @throws IllegalArgumentException when the capacity is less than zero.
     */
    public HashMap(int capacity) {
        this(capacity, 0.75f);  // default load factor of 0.75
    }

    /**
     * Calculates the capacity of storage required for storing given number of elements
     *
     * @param x number of elements
     * @return storage size
     */
    private static int calculateCapacity(int x) {
        if (x >= 1 << 30) {
            return 1 << 30;
        }
        if (x == 0) {
            return 16;
        }
        x = x - 1;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x + 1;
    }

    /**
     * Constructs a new {@code HashMap} instance with the specified capacity and load factor.
     *
     * @param capacity   the initial capacity of this hash map.
     * @param loadFactor the initial load factor.
     * @throws IllegalArgumentException when the capacity is less than zero or the load factor is less or equal to
     *                                  zero.
     */
    public HashMap(int capacity, float loadFactor) {
        if (capacity >= 0 && loadFactor > 0) {
            capacity = calculateCapacity(capacity);
            elementCount = 0;
            elementData = newElementArray(capacity);
            this.loadFactor = loadFactor;
            computeThreshold();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Constructs a new {@code HashMap} instance containing the mappings from the specified map.
     * <p/>
     * Changes from the java.util version:  The constructor there supported covariance, taking a Map&lt;? extends K, ?
     * extends V&gt; argument where the keys & values can be subclasses of E and V.   This constructor only allows maps
     * of exact type K and V, but you can call the putAll method instead if really need to copy a map with subtypes.
     *
     * @param map the mappings to add.
     */
    public HashMap(Map<K, V> map) {
        this(calculateCapacity(map.size()));
        putAllImpl(map);
    }

    /**
     * Removes all mappings from this hash map, leaving it empty.
     *
     * @see #isEmpty
     * @see #size
     */
    @Override
    public void clear() {
        if (elementCount > 0) {
            elementCount = 0;

            //Arrays.fill(elementData, null);
            for (int i = 0; i < elementData.length; i++) {
                elementData[i] = null;
            }

            modCount++;
        }
    }

    /**
     * Computes the threshold for rehashing
     */
    private void computeThreshold() {
        threshold = (int) (elementData.length * loadFactor);
    }

    /**
     * Returns whether this map contains the specified key.
     *
     * @param key the key to search for.
     * @return {@code true} if this map contains the specified key, {@code false} otherwise.
     */
    @Override
    public boolean containsKey(Object key) {
        HashMapEntry<K, V> m = getEntry(key);
        return m != null;
    }

    /**
     * Returns whether this map contains the specified value.
     *
     * @param value the value to search for.
     * @return {@code true} if this map contains the specified value, {@code false} otherwise.
     */
    @Override
    public boolean containsValue(Object value) {
        if (value != null) {
            for (int i = 0; i < elementData.length; i++) {
                HashMapEntry<K, V> entry = elementData[i];
                while (entry != null) {
                    if (areEqualValues(value, entry.value)) {
                        return true;
                    }
                    entry = entry.next;
                }
            }
        } else {
            for (int i = 0; i < elementData.length; i++) {
                HashMapEntry<K, V> entry = elementData[i];
                while (entry != null) {
                    if (entry.value == null) {
                        return true;
                    }
                    entry = entry.next;
                }
            }
        }
        return false;
    }

    /**
     * Returns a set containing all of the mappings in this map. Each mapping is an instance of {@link MapEntry}. As the
     * set is backed by this map, changes in one will be reflected in the other.
     *
     * @return a set of the mappings.
     */
    @Override
    public Set<MapEntry<K, V>> entrySet() {
        return new HashMapEntrySet<K, V>(this);
    }

    /**
     * Returns the value of the mapping with the specified key.
     *
     * @param key the key.
     * @return the value of the mapping with the specified key, or {@code null} if no mapping for the specified key is
     * found.
     */
    @Override
    public V get(Object key) {
        HashMapEntry<K, V> m = getEntry(key);
        if (m != null) {
            return m.value;
        }
        return null;
    }

    final @Nullable HashMapEntry<K, V> getEntry(Object key) {
        HashMapEntry<K, V> m;
        if (key == null) {
            m = findNullKeyEntry();
        } else {
            int hash = computeHashCode(key);
            int index = hash & (elementData.length - 1);
            m = findNonNullKeyEntry(key, index, hash);
        }
        return m;
    }

    final @Nullable HashMapEntry<K, V> findNonNullKeyEntry(Object key, int index, int keyHash) {
        HashMapEntry<K, V> m = elementData[index];
        while (m != null
                && (m.origKeyHash != keyHash || !areEqualKeys(key, m.key))) {
            m = m.next;
        }
        return m;
    }

    final @Nullable HashMapEntry<K, V> findNullKeyEntry() {
        HashMapEntry<K, V> m = elementData[0];
        while (m != null && m.key != null)
            m = m.next;
        return m;
    }

    /**
     * Returns whether this map is empty.
     *
     * @return {@code true} if this map has no elements, {@code false} otherwise.
     * @see #size()
     */
    @Override
    public boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     * Returns a set of the keys contained in this map. The set is backed by this map so changes to one are reflected by
     * the other. The set does not support adding.
     *
     * @return a set of the keys.
     */
    @Override
    public Set<K> keySet() {
        if (keysSet == null)
            keysSet = new KeySet<K, V>(this);
        return keysSet;
    }

    private static class KeySet<K, V> extends Set<K> {
        private HashMap<K, V> hashMap;

        public KeySet(HashMap<K, V> hashMap) {
            this.hashMap = hashMap;
        }

        @Override public boolean contains(Object object) {
            return hashMap.containsKey(object);
        }

        @Override public int size() {
            return hashMap.size();
        }

        @Override public void clear() {
            hashMap.clear();
        }

        @Override public boolean add(K object) {
            throw new ProgrammerError("add method not supported for HashMap keySet");
        }

        @Override public boolean remove(K key) {
            HashMapEntry<K, V> entry = hashMap.removeEntry(key);
            return entry != null;
        }

        @Override public Iterator<K> iterator() {
            return new KeyIterator<K, V>(hashMap);
        }
    }

    /**
     * Maps the specified key to the specified value.
     *
     * @param key   the key.
     * @param value the value.
     * @return the value of any previous mapping with the specified key or {@code null} if there was no such mapping.
     */
    @Override
    public V put(K key, V value) {
        return putImpl(key, value);
    }

    V putImpl(K key, V value) {
        HashMapEntry<K, V> entry;
        if (key == null) {
            entry = findNullKeyEntry();
            if (entry == null) {
                modCount++;
                entry = createHashedEntry(null, 0, 0);
                if (++elementCount > threshold) {
                    rehash();
                }
            }
        } else {
            int hash = computeHashCode(key);
            int index = hash & (elementData.length - 1);
            entry = findNonNullKeyEntry(key, index, hash);
            if (entry == null) {
                modCount++;
                entry = createHashedEntry(key, index, hash);
                if (++elementCount > threshold) {
                    rehash();
                }
            }
        }

        V result = entry.value;
        entry.value = value;
        return result;
    }

    HashMapEntry<K, V> createHashedEntry(K key, int index, int hash) {
        HashMapEntry<K, V> entry = new HashMapEntry<K, V>(key, hash);
        entry.next = elementData[index];
        elementData[index] = entry;
        return entry;
    }

    /**
     * Copies all the mappings in the specified map to this map. These mappings will replace all mappings that this map
     * had for any of the keys currently in the given map.}
     *
     * @param map the map to copy mappings from.
     * @throws NullPointerException if {@code map} is {@code null}.
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        if (!map.isEmpty()) {
            putAllImpl(map);
        }
    }

    private <TOtherK extends K, TOtherV extends V> void putAllImpl(Map<TOtherK, TOtherV> map) {
        int capacity = elementCount + map.size();
        if (capacity > threshold) {
            rehash(capacity);
        }

        for (MapEntry<TOtherK, TOtherV> entry : map.entrySet()) {
            putImpl(entry.getKey(), entry.getValue());
        }
    }

    void rehash(int capacity) {
        int length = calculateCapacity((capacity == 0 ? 1 : capacity << 1));

        HashMapEntry<K, V>[] newData = newElementArray(length);
        for (int i = 0; i < elementData.length; i++) {
            HashMapEntry<K, V> entry = elementData[i];
            elementData[i] = null;
            while (entry != null) {
                int index = entry.origKeyHash & (length - 1);
                HashMapEntry<K, V> next = entry.next;
                entry.next = newData[index];
                newData[index] = entry;
                entry = next;
            }
        }
        elementData = newData;
        computeThreshold();
    }

    void rehash() {
        rehash(elementData.length);
    }

    /**
     * Removes the mapping with the specified key from this map.
     *
     * @param key the key of the mapping to remove.
     * @return the value of the removed mapping or {@code null} if no mapping for the specified key was found.
     */
    @Override
    public V remove(Object key) {
        HashMapEntry<K, V> entry = removeEntry(key);
        if (entry != null) {
            return entry.value;
        }
        return null;
    }

    /*
     * Remove the given entry from the hashmap.
     * Assumes that the entry is in the map.
     */
    final void removeEntry(HashMapEntry<K, V> entry) {
        int index = entry.origKeyHash & (elementData.length - 1);
        HashMapEntry<K, V> m = elementData[index];
        if (m == entry) {
            elementData[index] = entry.next;
        } else {
            while (m.next != entry) {
                m = m.next;
            }
            m.next = entry.next;

        }
        modCount++;
        elementCount--;
    }

    final HashMapEntry<K, V> removeEntry(Object key) {
        int index = 0;
        HashMapEntry<K, V> entry;
        HashMapEntry<K, V> last = null;
        if (key != null) {
            int hash = computeHashCode(key);
            index = hash & (elementData.length - 1);
            entry = elementData[index];
            while (entry != null && !(entry.origKeyHash == hash && areEqualKeys(key, entry.key))) {
                last = entry;
                entry = entry.next;
            }
        } else {
            entry = elementData[0];
            while (entry != null && entry.key != null) {
                last = entry;
                entry = entry.next;
            }
        }
        if (entry == null) {
            return null;
        }
        if (last == null) {
            elementData[index] = entry.next;
        } else {
            last.next = entry.next;
        }
        modCount++;
        elementCount--;
        return entry;
    }

    /**
     * Returns the number of elements in this map.
     *
     * @return the number of elements in this map.
     */
    @Override
    public int size() {
        return elementCount;
    }

    /**
     * Returns a collection of the values contained in this map. The collection is backed by this map so changes to one
     * are reflected by the other. The collection supports remove, removeAll, retainAll and clear operations, and it
     * does not support add or addAll operations.
     * <p/>
     * This method returns a collection which is the subclass of AbstractCollection. The iterator method of this
     * subclass returns a "wrapper object" over the iterator of map's entrySet(). The {@code size} method wraps the
     * map's size method and the {@code contains} method wraps the map's containsValue method.
     * <p/>
     * The collection is created when this method is called for the first time and returned in response to all
     * subsequent calls. This method may return different collections when multiple concurrent calls occur, since no
     * synchronization is performed.
     *
     * @return a collection of the values contained in this map.
     */
    @Override
    public Collection<V> values() {
        if (valuesCollection == null)
            valuesCollection = new ValuesCollection<K, V>(this);
        return valuesCollection;
    }

    private static class ValuesCollection<K, V> extends Collection<V> {
        private HashMap<K, V> hashMap;

        private ValuesCollection(HashMap<K, V> hashMap) {
            this.hashMap = hashMap;
        }

        @Override public boolean contains(V object) {
            return hashMap.containsValue(object);
        }

        @Override public int size() {
            return hashMap.size();
        }

        @Override public void clear() {
            hashMap.clear();
        }

        @Override public Iterator<V> iterator() {
            return new ValueIterator<K, V>(hashMap);
        }

        @Override public boolean add(V object) {
            throw new ProgrammerError("add method not supported for HashMap.values collection");
        }
    }

    /*
     * Contract-related functionality 
     */
    static int computeHashCode(Object key) {
        return key.hashCode();
    }

    static boolean areEqualKeys(Object key1, Object key2) {
        return (key1 == key2) || key1.equals(key2);
    }

    static boolean areEqualValues(Object value1, Object value2) {
        return (value1 == value2) || value1.equals(value2);
    }

    static private boolean areEqual(Object object1, Object object2) {
        return (object1 == object2) || object1.equals(object2);
    }

    static private boolean areEqualNullsOK(@Nullable Object object1, @Nullable Object object2) {
        return (object1 == object2) || object1.equals(object2);
    }
}
