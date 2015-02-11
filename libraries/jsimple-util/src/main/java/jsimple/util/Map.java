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

import org.jetbrains.annotations.Nullable;

/**
 * A {@code Map} is a data structure consisting of a set of keys and values in which each key is mapped to a single
 * value.  The class of the objects used as keys is declared when the {@code Map} is declared, as is the class of the
 * corresponding values.
 * <p/>
 * A {@code Map} provides helper methods to iterate through all of the keys contained in it, as well as various methods
 * to access and update the key/value pairs.
 * <p/>
 * Changes from the java.util version:
 * <p/>
 * Does not implement Serializable nor clone()
 * <p/>
 * This class is an abstract implementation of the {@code Map} interface. This default implementation does not support
 * adding. A subclass must implement the abstract method entrySet().
 *
 * @since 1.2
 */
public abstract class Map<K, V> implements Equatable<Map<K, V>> {
    /**
     * Constructs a new instance of this {@code AbstractMap}.
     */
    protected Map() {
        super();
    }

    /**
     * Returns a {@code Set} containing all of the mappings in this {@code Map}. Each mapping is an instance of {@link
     * MapEntry}. As the {@code Set} is backed by this {@code Map}, changes in one will be reflected in the other.
     *
     * @return a set of the mappings
     */
    public abstract Set<MapEntry<K, V>> entrySet();

    /**
     * The default Java equals(Object) method isn't supported for JSimple maps.   Instead you should use
     * equals(Map&lt;K, V&gt;).
     * <p/>
     * Background:  The reason for this is the the default Java version isn't typesafe and with casting from Object
     * (both for the map itself and its members) it's too hard to make everything work properly with translation to C# /
     * other languages that have reified (that is, "real", not type erased) generics and value types, with consistent
     * semantics around covariance and avoiding the performance hit of boxing for value types. Better to avoid all that
     * and go with the more modern, typesafe version of equals.   It can perform slightly better even in straight Java
     * too.
     *
     * @param object the object to compare to this object
     * @return always throws an exception; don't use this method and use the typesafe equals(List&lt;K, V&gt;) equals
     * instead
     */
    @Override
    public boolean equals(Object object) {
        throw new ProgrammerError("equals isn't supported by default for collections;  use == for reference equality or implement your own method for other types of equality");
    }

    /**
     * Two maps are considered equal if they have the same number of entries and those entries have keys and values that
     * are equal, according to equals.   That's so-called deep equality.
     *
     * @param otherMap map to compare against
     * @return true if and only if the maps are equal
     */
    @Override public boolean equalTo(@Nullable Map<K, V> otherMap) {
        if (this == otherMap) {
            return true;
        }

        if (otherMap == null)
            return false;

        if (size() != otherMap.size())
            return false;

        for (MapEntry<K, V> entry : entrySet()) {
            K myKey = entry.getKey();
            V myValue = entry.getValue();
            V otherValue = otherMap.get(myKey);

            if (PlatformUtils.isNullOrTypeDefault(myValue)) {
                if (! PlatformUtils.isNullOrTypeDefault(otherValue) || !otherMap.containsKey(myKey)) {
                    return false;
                }
            } else if (!myValue.equals(otherValue)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the hash code for this object. Objects which are equal must return the same value for this method.
     *
     * @return the hash code of this object.
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        int result = 0;
        Iterator<MapEntry<K, V>> it = entrySet().iterator();
        while (it.hasNext()) {
            result += it.next().hashCode();
        }
        return result;
    }

    /**
     * Returns the string representation of this map.
     * <p/>
     * Note that this method doesn't support maps with a loop in the object graph that end up containing themselves
     * (e.g. map that directly or indirectly points back to itself as a key or value in the map). Such maps will result
     * in infinite recursion and ultimately a stack overflow exception.
     *
     * @return the string representation of this map.
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}"; //$NON-NLS-1$
        }

        StringBuilder buffer = new StringBuilder(size() * 28);
        buffer.append('{');
        Iterator<MapEntry<K, V>> it = entrySet().iterator();
        while (it.hasNext()) {
            MapEntry<K, V> entry = it.next();
            K key = entry.getKey();

            // The original Harmony source checked if key == this or value == this to avoid infinite recursion if a
            // collection includes itself.   That check handled the case of a single level of looping in the object
            // graph, but not nested looping (collection including a collection that includes itself).   In any case,
            // I had to remove that check to make the generics work with value types.  Not a huge deal, but noted here.

            buffer.append(key);
            buffer.append('=');

            V value = entry.getValue();
            buffer.append(value);

            if (it.hasNext()) {
                buffer.append(", "); //$NON-NLS-1$
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    /**
     * Removes all elements from this {@code Map}, leaving it empty.
     *
     * @throws UnsupportedOperationException if removing elements from this {@code Map} is not supported.
     * @see #isEmpty()
     * @see #size()
     */
    public abstract void clear();

    /**
     * Returns whether this {@code Map} contains the specified key.
     *
     * @param key the key to search for.
     * @return {@code true} if this map contains the specified key, {@code false} otherwise.
     */
    public abstract boolean containsKey(K key);

    /**
     * Returns whether this {@code Map} contains the specified value.
     *
     * @param value the value to search for.
     * @return {@code true} if this map contains the specified value, {@code false} otherwise.
     */
    public abstract boolean containsValue(V value);

    /**
     * Returns the value of the mapping with the specified key
     *
     * @param key the key.
     * @return the value of the mapping with the specified key, or {@code null} / default value for non-nullable value
     * type if no mapping for the specified key is found.
     */
    public abstract V get(K key);

    /**
     * Returns whether this map is empty.
     *
     * @return {@code true} if this map has no elements, {@code false} otherwise.
     * @see #size()
     */
    public abstract boolean isEmpty();

    /**
     * Returns a set of the keys contained in this {@code Map}. The {@code Set} is backed by this {@code Map} so changes
     * to one are reflected by the other. The {@code Set} does not support adding.
     *
     * @return a set of the keys.
     */
    public abstract Set<K> keySet();

    /**
     * Maps the specified key to the specified value.
     *
     * @param key   the key.
     * @param value the value.
     * @return the value of any previous mapping with the specified key or {@code null} / default value for non-nullable
     * value type if there was no mapping.
     * @throws UnsupportedOperationException if adding to this {@code Map} is not supported.
     * @throws ClassCastException            if the class of the key or value is inappropriate for this {@code Map}.
     * @throws IllegalArgumentException      if the key or value cannot be added to this {@code Map}.
     * @throws NullPointerException          if the key or value is {@code null} and this {@code Map} does not support
     *                                       {@code null} keys or values.
     */
    public abstract V put(K key, V value);

    /**
     * Copies every mapping in the specified {@code Map} to this {@code Map}.
     *
     * @param map the {@code Map} to copy mappings from.
     * @throws UnsupportedOperationException if adding to this {@code Map} is not supported.
     * @throws ClassCastException            if the class of a key or a value of the specified {@code Map} is
     *                                       inappropriate for this {@code Map}.
     * @throws IllegalArgumentException      if a key or value cannot be added to this {@code Map}.
     * @throws NullPointerException          if a key or value is {@code null} and this {@code Map} does not support
     *                                       {@code null} keys or values.
     */
    public abstract void putAll(Map<? extends K, ? extends V> map);

    /**
     * Removes a mapping with the specified key from this {@code Map}.
     *
     * @param key the key of the mapping to remove.
     * @return the value of the removed mapping or {@code null} / default value for non-nullable value type if no
     * mapping for the specified key was found
     * @throws UnsupportedOperationException if removing from this {@code Map} is not supported.
     */
    public abstract V remove(K key);

    /**
     * Returns the number of mappings in this {@code Map}.
     *
     * @return the number of mappings in this {@code Map}.
     */
    public abstract int size();

    /**
     * Returns a {@code Collection} of the values contained in this {@code Map}. The {@code Collection} is backed by
     * this {@code Map} so changes to one are reflected by the other. The {@code Collection} supports {@link
     * Collection#remove}, {@link Collection#removeAll}, {@link Collection#retainAll}, and {@link Collection#clear}
     * operations, and it does not support {@link Collection#add} or {@link Collection#addAll} operations.
     * <p/>
     * This method returns a {@code Collection} which is the subclass of {@link Collection}. The {@link
     * Collection#iterator} method of this subclass returns a "wrapper object" over the iterator of this {@code Map}'s
     * {@link #entrySet()}. The {@link Collection#size} method wraps this {@code Map}'s {@link #size} method and the
     * {@link Collection#contains} method wraps this {@code Map}'s {@link #containsValue} method.
     * <p/>
     * The collection is created when this method is called at first time and returned in response to all subsequent
     * calls. This method may return different Collection when multiple calls to this method, since it has no
     * synchronization performed.
     *
     * @return a collection of the values contained in this map.
     */
    public abstract Collection<V> values();
}
