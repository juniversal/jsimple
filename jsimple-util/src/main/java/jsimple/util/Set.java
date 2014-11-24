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


import java.util.Iterator;

/**
 * A {@code Set} is a data structure which does not allow duplicate elements.
 *
 * @since 1.2
 */
public interface Set<E> extends Collection<E> {
    
    /**
     * Adds the specified object to this set. The set is not modified if it
     * already contains the object.
     * 
     * @param object
     *            the object to add.
     * @return {@code true} if this set is modified, {@code false} otherwise.
     * @throws UnsupportedOperationException
     *             when adding to this set is not supported.
     * @throws ClassCastException
     *             when the class of the object is inappropriate for this set.
     * @throws IllegalArgumentException
     *             when the object cannot be added to this set.
     */
    public boolean add(E object);

    /**
     * Adds the objects in the specified collection which do not exist yet in
     * this set.
     * 
     * @param collection
     *            the collection of objects.
     * @return {@code true} if this set is modified, {@code false} otherwise.
     * @throws UnsupportedOperationException
     *             when adding to this set is not supported.
     * @throws ClassCastException
     *             when the class of an object is inappropriate for this set.
     * @throws IllegalArgumentException
     *             when an object cannot be added to this set.
     */
    public boolean addAll(Collection<? extends E> collection);

    /**
     * Removes all elements from this set, leaving it empty.
     * 
     * @throws UnsupportedOperationException
     *             when removing from this set is not supported.
     * @see #isEmpty
     * @see #size
     */
    public void clear();

    /**
     * Searches this set for the specified object.
     * 
     * @param object
     *            the object to search for.
     * @return {@code true} if object is an element of this set, {@code false}
     *         otherwise.
     */
    public boolean contains(E object);

    /**
     * Searches this set for all objects in the specified collection.
     * 
     * @param collection
     *            the collection of objects.
     * @return {@code true} if all objects in the specified collection are
     *         elements of this set, {@code false} otherwise.
     */
    public boolean containsAll(Collection<E> collection);

    /**
     * Compares the specified object to this set, and returns true if they
     * represent the <em>same</em> object using a class specific comparison.
     * Equality for a set means that both sets have the same size and the same
     * elements.
     * 
     * @param object
     *            the object to compare with this object.
     * @return boolean {@code true} if the object is the same as this object,
     *         and {@code false} if it is different from this object.
     * @see #hashCode
     */
    public boolean equals(Object object);

    /**
     * Returns the hash code for this set. Two set which are equal must return
     * the same value.
     * 
     * @return the hash code of this set.
     * 
     * @see #equals
     */
    public int hashCode();

    /**
     * Returns true if this set has no elements.
     * 
     * @return {@code true} if this set has no elements, {@code false}
     *         otherwise.
     * @see #size
     */
    public boolean isEmpty();

    /**
     * Returns an iterator on the elements of this set. The elements are
     * unordered.
     * 
     * @return an iterator on the elements of this set.
     * @see Iterator
     */
    public Iterator<E> iterator();

    /**
     * Removes the specified object from this set.
     * 
     * @param object
     *            the object to remove.
     * @return {@code true} if this set was modified, {@code false} otherwise.
     * @throws UnsupportedOperationException
     *             when removing from this set is not supported.
     */
    public boolean remove(E object);

    /**
     * Removes all objects in the specified collection from this set.
     * 
     * @param collection
     *            the collection of objects to remove.
     * @return {@code true} if this set was modified, {@code false} otherwise.
     * @throws UnsupportedOperationException
     *             when removing from this set is not supported.
     */
    public boolean removeAll(Collection<E> collection);

    /**
     * Removes all objects from this set that are not contained in the specified
     * collection.
     * 
     * @param collection
     *            the collection of objects to retain.
     * @return {@code true} if this set was modified, {@code false} otherwise.
     * @throws UnsupportedOperationException
     *             when removing from this set is not supported.
     */
    public boolean retainAll(Collection<E> collection);

    /**
     * Returns the number of elements in this set.
     * 
     * @return the number of elements in this set.
     */
    public int size();

    /**
     * Returns an array containing all elements contained in this set.
     * 
     * @return an array of the elements from this set.
     */
    public Object[] toArray();

    /**
     * Copies all elements contained in this {@code Collection} to the specified array.   Unlike the regular java.lang
     * version of this method, here the array is never allocated & returned but instead a big enough array must be passed
     * in.   If the specified array isn't big enough to hold all elements, an exception is thrown.   If it's bigger than
     * needed, the array element following the {@code Collection} elements is set to null.
     * <p>
     * Changes to the standard java.lang version of this method avoid the need for reflection and make it a little
     * simpler.   Typical usage would be to just allocate an array of exactly the right length, with size() elements.
     *
     * @param array the array.
     */
    public <T> void toArray(T[] array);
}
