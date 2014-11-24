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

import java.util.Iterator;

/**
 * Changes from the java.lang version:
 * Does not implement Serializable
 * <p/>
 * This class is an abstract implementation of the {@code Map} interface. This
 * implementation does not support adding. A subclass must implement the
 * abstract method entrySet().
 *
 * @since 1.2
 */
public abstract class AbstractMap<K, V> implements Map<K, V> {
    // Lazily initialized key set.
    Set<K> keySet;

    Collection<V> valuesCollection;

    /**
     * Constructs a new instance of this {@code AbstractMap}.
     */
    protected AbstractMap() {
        super();
    }

    /**
     * Returns a set containing all of the mappings in this map. Each mapping is
     * an instance of {@link MapEntry}. As the set is backed by this map,
     * changes in one will be reflected in the other.
     *
     * @return a set of the mappings.
     */
    public abstract Set<MapEntry<K, V>> entrySet();

    /**
     * By default, equals isn't supported for maps or other collections, though subclasses can choose to override this
     * behavior if they want.   Instead developers should call some other, more specific, method, checking for the exact
     * kind of equality they want.
     * <p/>
     * As background:  This behavior differs from the standard java.util.AbstractMaps.equals method, where two maps are
     * considered equal if the have the same number of entries and those entries have keys and values that are equal,
     * according to equals.   That's so-called deep equality.   Whereas C#, on the other hand, just does reference equality
     * by default for maps, saying two maps are equal if they reference the same object.
     * <p/>
     * There are two main reasons we differ from standard Java and don't support equals here or for other collection
     * methods:
     * <p/>
     * (a) The desired semantics are ambiguous (deep, shallow, or reference equality?  should order matter?  map type matter?).
     * There's no single obvious right answer & different semantics are appropriate in different cases.
     * (b) It's hard to implement the Java semantics and support generics, with covariance, properly and have it work
     * with Java to C#/Swift translation.  Limited C# and Swift support for covariance, especially when casting from an
     * Object, makes it all the harder.
     * So best to just avoid all those issues & make the developer do explicitly what they want, calling some other
     * method.
     *
     * @param object the object to compare to this object.
     * @return {@code true} if the specified object is equal to this list, {@code false} otherwise; though by default
     * this method throws an exception and developers generally shouldn't use it
     */
    @Override
    public boolean equals(Object object) {
        throw new ProgrammerError("equals isn't supported by default for collections;  use == for reference equality or implement your own method for other types of equality");
        /*
        if (this == object) {
            return true;
        }
        if (object instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) object;
            if (size() != map.size()) {
                return false;
            }

            try {
                for (Entry<K, V> entry : entrySet()) {
                    K key = entry.getKey();
                    V mine = entry.getValue();
                    Object theirs = map.get(key);
                    if (mine == null) {
                        if (theirs != null || !map.containsKey(key)) {
                            return false;
                        }
                    } else if (!mine.equals(theirs)) {
                        return false;
                    }
                }
            } catch (NullPointerException ignored) {
                return false;
            } catch (ClassCastException ignored) {
                return false;
            }
            return true;
        }
        return false;
        */
    }

    /**
     * Returns the hash code for this object. Objects which are equal must
     * return the same value for this method.
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
            Object key = entry.getKey();
            if (key != this) {
                buffer.append(key);
            } else {
                buffer.append("(this Map)"); //$NON-NLS-1$
            }
            buffer.append('=');
            Object value = entry.getValue();
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Map)"); //$NON-NLS-1$
            }
            if (it.hasNext()) {
                buffer.append(", "); //$NON-NLS-1$
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    /**
     * Returns a new instance of the same class as this instance, whose slots
     * have been filled in with the values of the slots of this instance.
     *
     * @return a shallow copy of this object.
     * @throws CloneNotSupportedException if the receiver's class does not implement the interface
     *                                    {@code Cloneable}.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected Object clone() throws CloneNotSupportedException {
        AbstractMap<K, V> result = (AbstractMap<K, V>) super.clone();
        result.keySet = null;
        result.valuesCollection = null;
        return result;
    }
}
