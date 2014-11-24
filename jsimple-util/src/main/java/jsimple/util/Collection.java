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
 * {@code Collection} is the root of the collection hierarchy. It defines operations on
 * data collections and the behavior that they will have in all implementations
 * of {@code Collection}s.
 * <p>
 * All direct or indirect implementations of {@code Collection} should implement at
 * least two constructors. One with no parameters which creates an empty
 * collection and one with a parameter of type {@code Collection}. This second
 * constructor can be used to create a collection of different type as the
 * initial collection but with the same elements. Implementations of {@code Collection}
 * cannot be forced to implement these two constructors but at least all
 * implementations under {@code java.util} do.
 * <p>
 * Methods that change the content of a collection throw an
 * {@code UnsupportedOperationException} if the underlying collection does not
 * support that operation, though it's not mandatory to throw such an {@code Exception}
 * in cases where the requested operation would not change the collection. In
 * these cases it's up to the implementation whether it throws an
 * {@code UnsupportedOperationException} or not.
 * <p>
 * Methods marked with (optional) can throw an
 * {@code UnsupportedOperationException} if the underlying collection doesn't
 * support that method.
 */
public interface Collection<E> extends Iterable<E> {

    /**
     * Attempts to add {@code object} to the contents of this
     * {@code Collection} (optional).
     * <p>
     * After this method finishes successfully it is guaranteed that the object
     * is contained in the collection.
     * <p>
     * If the collection was modified it returns {@code true}, {@code false} if
     * no changes were made.
     * <p>
     * An implementation of {@code Collection} may narrow the set of accepted
     * objects, but it has to specify this in the documentation. If the object
     * to be added does not meet this restriction, then an
     * {@code IllegalArgumentException} is thrown.
     * <p>
     * If a collection does not yet contain an object that is to be added and
     * adding the object fails, this method <i>must</i> throw an appropriate
     * unchecked Exception. Returning false is not permitted in this case
     * because it would violate the postcondition that the element will be part
     * of the collection after this method finishes.
     *
     * @param object the object to add.
     * @return {@code true} if this {@code Collection} is
     * modified, {@code false} otherwise.
     * @throws UnsupportedOperationException if adding to this {@code Collection} is not supported.
     * @throws ClassCastException            if the class of the object is inappropriate for this
     *                                       collection.
     * @throws IllegalArgumentException      if the object cannot be added to this {@code Collection}.
     * @throws NullPointerException          if null elements cannot be added to the {@code Collection}.
     */
    public boolean add(E object);

    /**
     * Attempts to add all of the objects contained in {@code Collection}
     * to the contents of this {@code Collection} (optional). If the passed {@code Collection}
     * is changed during the process of adding elements to this {@code Collection}, the
     * behavior is not defined.
     *
     * @param collection the {@code Collection} of objects.
     * @return {@code true} if this {@code Collection} is modified, {@code false}
     * otherwise.
     * @throws UnsupportedOperationException if adding to this {@code Collection} is not supported.
     * @throws ClassCastException            if the class of an object is inappropriate for this
     *                                       {@code Collection}.
     * @throws IllegalArgumentException      if an object cannot be added to this {@code Collection}.
     * @throws NullPointerException          if {@code collection} is {@code null}, or if it
     *                                       contains {@code null} elements and this {@code Collection} does
     *                                       not support such elements.
     */
    public boolean addAll(Collection<? extends E> collection);

    /**
     * Removes all elements from this {@code Collection}, leaving it empty (optional).
     *
     * @throws UnsupportedOperationException if removing from this {@code Collection} is not supported.
     * @see #isEmpty
     * @see #size
     */
    public void clear();

    /**
     * Tests whether this {@code Collection} contains the specified object. Returns
     * {@code true} if and only if at least one element {@code elem} in this
     * {@code Collection} meets following requirement:
     * {@code (object==null ? elem==null : object.equals(elem))}.
     *
     * @param object the object to search for.
     * @return {@code true} if object is an element of this {@code Collection},
     * {@code false} otherwise.
     * @throws ClassCastException   if the object to look for isn't of the correct
     *                              type.
     * @throws NullPointerException if the object to look for is {@code null} and this
     *                              {@code Collection} doesn't support {@code null} elements.
     */
    public boolean contains(E object);

    /**
     * Tests whether this {@code Collection} contains all objects contained in the
     * specified {@code Collection}. If an element {@code elem} is contained several
     * times in the specified {@code Collection}, the method returns {@code true} even
     * if {@code elem} is contained only once in this {@code Collection}.
     *
     * @param collection the collection of objects.
     * @return {@code true} if all objects in the specified {@code Collection} are
     * elements of this {@code Collection}, {@code false} otherwise.
     * @throws ClassCastException   if one or more elements of {@code collection} isn't of the
     *                              correct type.
     * @throws NullPointerException if {@code collection} contains at least one {@code null}
     *                              element and this {@code Collection} doesn't support {@code null}
     *                              elements.
     * @throws NullPointerException if {@code collection} is {@code null}.
     */
    public boolean containsAll(Collection<E> collection);

    /**
     * Compares the argument to the receiver, and returns true if they represent
     * the <em>same</em> object using a class specific comparison.
     *
     * @param object the object to compare with this object.
     * @return {@code true} if the object is the same as this object and
     * {@code false} if it is different from this object.
     * @see #hashCode
     */
    public boolean equals(Object object);

    /**
     * Returns an integer hash code for the receiver. Objects which are equal
     * return the same value for this method.
     *
     * @return the receiver's hash.
     * @see #equals
     */
    public int hashCode();

    /**
     * Returns if this {@code Collection} contains no elements.
     *
     * @return {@code true} if this {@code Collection} has no elements, {@code false}
     * otherwise.
     * @see #size
     */
    public boolean isEmpty();

    /**
     * Returns an instance of {@link Iterator} that may be used to access the
     * objects contained by this {@code Collection}. The order in which the elements are
     * returned by the iterator is not defined. Only if the instance of the
     * {@code Collection} has a defined order the elements are returned in that order.
     *
     * @return an iterator for accessing the {@code Collection} contents.
     */
    public Iterator<E> iterator();

    /**
     * Removes one instance of the specified object from this {@code Collection} if one
     * is contained (optional). The element {@code elem} that is removed
     * complies with {@code (object==null ? elem==null : object.equals(elem)}.
     *
     * @param object the object to remove.
     * @return {@code true} if this {@code Collection} is modified, {@code false}
     * otherwise.
     * @throws UnsupportedOperationException if removing from this {@code Collection} is not supported.
     * @throws ClassCastException            if the object passed is not of the correct type.
     * @throws NullPointerException          if {@code object} is {@code null} and this {@code Collection}
     *                                       doesn't support {@code null} elements.
     */
    public boolean remove(E object);

    /**
     * Removes all occurrences in this {@code Collection} of each object in the
     * specified {@code Collection} (optional). After this method returns none of the
     * elements in the passed {@code Collection} can be found in this {@code Collection}
     * anymore.
     *
     * @param collection the collection of objects to remove.
     * @return {@code true} if this {@code Collection} is modified, {@code false}
     * otherwise.
     * @throws UnsupportedOperationException if removing from this {@code Collection} is not supported.
     * @throws ClassCastException            if one or more elements of {@code collection}
     *                                       isn't of the correct type.
     * @throws NullPointerException          if {@code collection} contains at least one
     *                                       {@code null} element and this {@code Collection} doesn't support
     *                                       {@code null} elements.
     * @throws NullPointerException          if {@code collection} is {@code null}.
     */
    public boolean removeAll(Collection<E> collection);

    /**
     * Removes all objects from this {@code Collection} that are not also found in the
     * {@code Collection} passed (optional). After this method returns this {@code Collection}
     * will only contain elements that also can be found in the {@code Collection}
     * passed to this method.
     *
     * @param collection the collection of objects to retain.
     * @return {@code true} if this {@code Collection} is modified, {@code false}
     * otherwise.
     * @throws UnsupportedOperationException if removing from this {@code Collection} is not supported.
     * @throws ClassCastException            if one or more elements of {@code collection}
     *                                       isn't of the correct type.
     * @throws NullPointerException          if {@code collection} contains at least one
     *                                       {@code null} element and this {@code Collection} doesn't support
     *                                       {@code null} elements.
     * @throws NullPointerException          if {@code collection} is {@code null}.
     */
    public boolean retainAll(Collection<E> collection);

    /**
     * Returns a count of how many objects this {@code Collection} contains.
     *
     * @return how many objects this {@code Collection} contains, or Integer.MAX_VALUE
     * if there are more than Integer.MAX_VALUE elements in this
     * {@code Collection}.
     */
    public int size();

    /**
     * Returns a new array containing all elements contained in this {@code Collection}.
     * <p>
     * If the implementation has ordered elements it will return the element
     * array in the same order as an iterator would return them.
     * <p>
     * The array returned does not reflect any changes of the {@code Collection}. A new
     * array is created even if the underlying data structure is already an
     * array.
     *
     * @return an array of the elements from this {@code Collection}.
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
