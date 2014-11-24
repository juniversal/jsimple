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
 * A {@code List} is a collection which maintains an ordering for its elements. Every
 * element in the {@code List} has an index. Each element can thus be accessed by its
 * index, with the first index being zero. Normally, {@code List}s allow duplicate
 * elements, as compared to Sets, where elements have to be unique.
 */
public interface List<E> extends Collection<E> {
    /**
     * Inserts the specified object into this {@code List} at the specified location.
     * The object is inserted before the current element at the specified
     * location. If the location is equal to the size of this {@code List}, the object
     * is added at the end. If the location is smaller than the size of this
     * {@code List}, then all elements beyond the specified location are moved by one
     * position towards the end of the {@code List}.
     * 
     * @param location
     *            the index at which to insert.
     * @param object
     *            the object to add.
     * @throws UnsupportedOperationException
     *                if adding to this {@code List} is not supported.
     * @throws ClassCastException
     *                if the class of the object is inappropriate for this
     *                {@code List}.
     * @throws IllegalArgumentException
     *                if the object cannot be added to this {@code List}.
     * @throws IndexOutOfBoundsException
     *                if {@code location < 0 || location > size()}
     */
    public void add(int location, E object);

    /**
     * Adds the specified object at the end of this {@code List}.
     * 
     * @param object
     *            the object to add.
     * @return always true.
     * @throws UnsupportedOperationException
     *                if adding to this {@code List} is not supported.
     * @throws ClassCastException
     *                if the class of the object is inappropriate for this
     *                {@code List}.
     * @throws IllegalArgumentException
     *                if the object cannot be added to this {@code List}.
     */
    public boolean add(E object);

    /**
     * Inserts the objects in the specified collection at the specified location
     * in this {@code List}. The objects are added in the order they are returned from
     * the collection's iterator.
     * 
     * @param location
     *            the index at which to insert.
     * @param collection
     *            the collection of objects to be inserted.
     * @return true if this {@code List} has been modified through the insertion, false
     *         otherwise (i.e. if the passed collection was empty).
     * @throws UnsupportedOperationException
     *                if adding to this {@code List} is not supported.
     * @throws ClassCastException
     *                if the class of an object is inappropriate for this
     *                {@code List}.
     * @throws IllegalArgumentException
     *                if an object cannot be added to this {@code List}.
     * @throws IndexOutOfBoundsException
     *                if {@code location < 0 || > size()}
     */
    public boolean addAll(int location, Collection<? extends E> collection);

    /**
     * Adds the objects in the specified collection to the end of this {@code List}. The
     * objects are added in the order in which they are returned from the
     * collection's iterator.
     * 
     * @param collection
     *            the collection of objects.
     * @return {@code true} if this {@code List} is modified, {@code false} otherwise
     *         (i.e. if the passed collection was empty).
     * @throws UnsupportedOperationException
     *                if adding to this {@code List} is not supported.
     * @throws ClassCastException
     *                if the class of an object is inappropriate for this
     *                {@code List}.
     * @throws IllegalArgumentException
     *                if an object cannot be added to this {@code List}.
     */
    public boolean addAll(Collection<? extends E> collection);

    /**
     * Removes all elements from this {@code List}, leaving it empty.
     * 
     * @throws UnsupportedOperationException
     *                if removing from this {@code List} is not supported.
     * @see #isEmpty
     * @see #size
     */
    public void clear();

    /**
     * Tests whether this {@code List} contains the specified object.
     * 
     * @param object
     *            the object to search for.
     * @return {@code true} if object is an element of this {@code List}, {@code false}
     *         otherwise
     */
    public boolean contains(E object);

    /**
     * Tests whether this {@code List} contains all objects contained in the
     * specified collection.
     * 
     * @param collection
     *            the collection of objects
     * @return {@code true} if all objects in the specified collection are
     *         elements of this {@code List}, {@code false} otherwise.
     */
    public boolean containsAll(Collection<E> collection);

    /**
     * Compares the given object with the {@code List}, and returns true if they
     * represent the <em>same</em> object using a class specific comparison. For
     * {@code List}s, this means that they contain the same elements in exactly the same
     * order.
     * 
     * @param object
     *            the object to compare with this object.
     * @return boolean {@code true} if the object is the same as this object,
     *         and {@code false} if it is different from this object.
     * @see #hashCode
     */
    public boolean equals(Object object);

    /**
     * Returns the element at the specified location in this {@code List}.
     * 
     * @param location
     *            the index of the element to return.
     * @return the element at the specified location.
     * @throws IndexOutOfBoundsException
     *                if {@code location < 0 || >= size()}
     */
    public E get(int location);

    /**
     * Returns the hash code for this {@code List}. It is calculated by taking each
     * element' hashcode and its position in the {@code List} into account.
     * 
     * @return the hash code of the {@code List}.
     */
    public int hashCode();

    /**
     * Searches this {@code List} for the specified object and returns the index of the
     * first occurrence.
     * 
     * @param object
     *            the object to search for.
     * @return the index of the first occurrence of the object or -1 if the
     *         object was not found.
     */
    public int indexOf(Object object);

    /**
     * Returns whether this {@code List} contains no elements.
     * 
     * @return {@code true} if this {@code List} has no elements, {@code false}
     *         otherwise.
     * @see #size
     */
    public boolean isEmpty();

    /**
     * Returns an iterator on the elements of this {@code List}. The elements are
     * iterated in the same order as they occur in the {@code List}.
     * 
     * @return an iterator on the elements of this {@code List}.
     * @see Iterator
     */
    public Iterator<E> iterator();

    /**
     * Searches this {@code List} for the specified object and returns the index of the
     * last occurrence.
     * 
     * @param object
     *            the object to search for.
     * @return the index of the last occurrence of the object, or -1 if the
     *         object was not found.
     */
    public int lastIndexOf(Object object);

    /**
     * Removes the object at the specified location from this {@code List}.
     * 
     * @param location
     *            the index of the object to remove.
     * @return the removed object.
     * @throws UnsupportedOperationException
     *                if removing from this {@code List} is not supported.
     * @throws IndexOutOfBoundsException
     *                if {@code location < 0 || >= size()}
     */
    public E remove(int location);

    /**
     * Removes the first occurrence of the specified object from this {@code List}.
     * 
     * @param object
     *            the object to remove.
     * @return true if this {@code List} was modified by this operation, false
     *         otherwise.
     * @throws UnsupportedOperationException
     *                if removing from this {@code List} is not supported.
     */
    public boolean remove(E object);

    /**
     * Removes all occurrences in this {@code List} of each object in the specified
     * collection.
     * 
     * @param collection
     *            the collection of objects to remove.
     * @return {@code true} if this {@code List} is modified, {@code false} otherwise.
     * @throws UnsupportedOperationException
     *                if removing from this {@code List} is not supported.
     */
    public boolean removeAll(Collection<E> collection);

    /**
     * Removes all objects from this {@code List} that are not contained in the
     * specified collection.
     * 
     * @param collection
     *            the collection of objects to retain.
     * @return {@code true} if this {@code List} is modified, {@code false} otherwise.
     * @throws UnsupportedOperationException
     *                if removing from this {@code List} is not supported.
     */
    public boolean retainAll(Collection<E> collection);

    /**
     * Replaces the element at the specified location in this {@code List} with the
     * specified object. This operation does not change the size of the {@code List}.
     * 
     * @param location
     *            the index at which to put the specified object.
     * @param object
     *            the object to insert.
     * @return the previous element at the index.
     * @throws UnsupportedOperationException
     *                if replacing elements in this {@code List} is not supported.
     * @throws ClassCastException
     *                if the class of an object is inappropriate for this
     *                {@code List}.
     * @throws IllegalArgumentException
     *                if an object cannot be added to this {@code List}.
     * @throws IndexOutOfBoundsException
     *                if {@code location < 0 || >= size()}
     */
    public E set(int location, E object);

    /**
     * Returns the number of elements in this {@code List}.
     * 
     * @return the number of elements in this {@code List}.
     */
    public int size();

    /**
     * Returns an array containing all elements contained in this {@code List}.
     * 
     * @return an array of the elements from this {@code List}.
     */
    public Object[] toArray();

    public <T> void toArray(T[] array);
}
