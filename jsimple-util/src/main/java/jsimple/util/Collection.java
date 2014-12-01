/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from Apache Harmony
 * http://harmony.apache.org (collectively, “Third Party Code”). Microsoft Mobile
 * is not the original author of the Third Party Code. The original copyright
 * notice and the license, under which Microsoft Mobile received such Third Party
 * Code, are set forth below.
 *
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


/**
 * {@code Collection} is the root of the collection hierarchy. It defines operations on data collections and the
 * behavior that they will have in all implementations of {@code Collection}s.
 * <p/>
 * All direct or indirect implementations of {@code Collection} should implement at least two constructors. One with no
 * parameters which creates an empty collection and one with a parameter of type {@code Collection}. This second
 * constructor can be used to create a collection of different type as the initial collection but with the same
 * elements. Implementations of {@code Collection} cannot be forced to implement these two constructors but at least all
 * implementations under {@code java.util} do.
 * <p/>
 * Methods that change the content of a collection throw an {@code UnsupportedOperationException} if the underlying
 * collection does not support that operation, though it's not mandatory to throw such an {@code Exception} in cases
 * where the requested operation would not change the collection. In these cases it's up to the implementation whether
 * it throws an {@code UnsupportedOperationException} or not.
 * <p/>
 * Methods marked with (optional) can throw an {@code UnsupportedOperationException} if the underlying collection
 * doesn't support that method.
 * <p/>
 * A subclass must implement the abstract methods {@code iterator()} and {@code size()} to create an immutable
 * collection. To create a modifiable collection it's necessary to override the {@code add()} method that currently
 * throws an {@code UnsupportedOperationException}.
 * <p/>
 * Changes from the java.lang version:
 * <p/>
 * <T> void toArray(T[] contents) requires an array of the right size to be passed in, never allocating the array. That
 * avoids the need for reflection & was the most efficient option for using that method anyway.
 * <p/>
 * The contains and remove methods take an E instead of an Object as a parameter.   Logically, this makes more sense, is
 * more type safe, and is consistent with C#.   This change avoids some generic casting issues.
 */
public abstract class Collection<E> extends jsimple.lang.Iterable<E> {
    /**
     * Constructs a new instance of this AbstractCollection.
     */
    protected Collection() {
        super();
    }

    /**
     * Attempts to add {@code object} to the contents of this {@code Collection} (optional).
     * <p/>
     * After this method finishes successfully it is guaranteed that the object is contained in the collection.
     * <p/>
     * If the collection was modified it returns {@code true}, {@code false} if no changes were made.
     * <p/>
     * An implementation of {@code Collection} may narrow the set of accepted objects, but it has to specify this in the
     * documentation. If the object to be added does not meet this restriction, then an {@code IllegalArgumentException}
     * is thrown.
     * <p/>
     * If a collection does not yet contain an object that is to be added and adding the object fails, this method
     * <i>must</i> throw an appropriate unchecked Exception. Returning false is not permitted in this case because it
     * would violate the postcondition that the element will be part of the collection after this method finishes.
     *
     * @param object the object to add.
     * @return {@code true} if this {@code Collection} is modified, {@code false} otherwise.
     * @throws UnsupportedOperationException if adding to this {@code Collection} is not supported.
     * @throws ClassCastException            if the class of the object is inappropriate for this collection.
     * @throws IllegalArgumentException      if the object cannot be added to this {@code Collection}.
     * @throws NullPointerException          if null elements cannot be added to the {@code Collection}.
     */
    public boolean add(E object) {
        throw new ProgrammerError("add is not supported for this kind of collection");
    }

    /**
     * Attempts to add all of the objects contained in {@code Collection} to the contents of this {@code Collection}
     * (optional). If the passed {@code Collection} is changed during the process of adding elements to this {@code
     * Collection}, the behavior is not defined.
     * <p/>
     * The default implementation iterates over the given {@code Collection} and calls {@code add} for each element. If
     * any of these calls return {@code true}, then {@code true} is returned as result of this method call, {@code
     * false} otherwise. If this {@code Collection} does not support adding elements, an {@code
     * UnsupportedOperationException} is thrown.
     *
     * @param collection the {@code Collection} of objects.
     * @return {@code true} if this {@code Collection} is modified, {@code false} otherwise.
     * @throws UnsupportedOperationException if adding to this {@code Collection} is not supported.
     * @throws ClassCastException            if the class of an object is inappropriate for this {@code Collection}.
     * @throws IllegalArgumentException      if an object cannot be added to this {@code Collection}.
     * @throws NullPointerException          if {@code collection} is {@code null}, or if it contains {@code null}
     *                                       elements and this {@code Collection} does not support such elements.
     */
    public boolean addAll(Collection<? extends E> collection) {
        boolean result = false;
        for (E item : collection) {
            if (add(item)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Removes all elements from this {@code Collection}, leaving it empty (optional).
     * <p/>
     * The default implementation iterates over this {@code Collection} and calls the {@code remove} method on each
     * element. If the iterator does not support removal of elements, an {@code UnsupportedOperationException} is
     * thrown.
     * <p/>
     * Concrete implementations usually can clear a {@code Collection} more efficiently and should therefore overwrite
     * this method.
     *
     * @throws UnsupportedOperationException it the iterator does not support removing elements from this {@code
     *                                       Collection}
     * @see #iterator
     * @see #isEmpty
     * @see #size
     */
    public void clear() {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    /**
     * Tests whether this {@code Collection} contains the specified object.  Returns {@code true} if and only if at
     * least one element {@code elem} in this {@code Collection} meets following requirement: {@code (object==null ?
     * elem==null : object.equals(elem))}.
     * <p/>
     * The default implementation iterates over this {@code Collection} and tests, whether any element is equal to the
     * given object. If {@code object != null} then {@code object.equals(e)} is called for each element {@code e}
     * returned by the iterator until the element is found. If {@code object == null} then each element {@code e}
     * returned by the iterator is compared with the test {@code e == null}.
     *
     * @param object the object to search for.
     * @return {@code true} if object is an element of this {@code Collection}, {@code false} otherwise.
     * @throws ClassCastException   if the object to look for isn't of the correct type.
     * @throws NullPointerException if the object to look for is {@code null} and this {@code Collection} doesn't
     *                              support {@code null} elements.
     */
    public boolean contains(E object) {
        if (object != null) {
            for (E e : this) {
                if (object.equals(e)) {
                    return true;
                }
            }
        } else {
            for (E e : this) {
                if (e == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Tests whether this {@code Collection} contains all objects contained in the specified {@code Collection}. If an
     * element {@code elem} is contained several times in the specified {@code Collection}, the method returns {@code
     * true} even if {@code elem} is contained only once in this {@code Collection}.
     * <p/>
     * The default implementation iterates over the specified {@code Collection}. If one element returned by the
     * iterator is not contained in this {@code Collection}, then {@code false} is returned; {@code true} otherwise.
     *
     * @param collection the collection of objects.
     * @return {@code true} if all objects in the specified {@code Collection} are elements of this {@code Collection},
     * {@code false} otherwise.
     * @throws ClassCastException   if one or more elements of {@code collection} isn't of the correct type.
     * @throws NullPointerException if {@code collection} contains at least one {@code null} element and this {@code
     *                              Collection} doesn't support {@code null} elements.
     * @throws NullPointerException if {@code collection} is {@code null}.
     */
    public boolean containsAll(Collection<E> collection) {
        for (E otherElmt : collection) {
            if (!contains(otherElmt)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns if this {@code Collection} contains no elements.
     * <p/>
     * The default implementation tests whether {@code size} returns 0.
     *
     * @return {@code true} if this {@code Collection} has no elements, {@code false} otherwise.
     * @see #size
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns an instance of {@link Iterator} that may be used to access the objects contained by this {@code
     * Collection}. The order in which the elements are returned by the {@link Iterator} is not defined unless the
     * instance of the {@code Collection} has a defined order.  In that case, the elements are returned in that order.
     *
     * @return an iterator for accessing the {@code Collection} contents.
     */
    @Override public abstract Iterator<E> iterator();

    /**
     * Removes one instance of the specified object from this {@code Collection} if one is contained (optional). The
     * element {@code elem} that is removed complies with {@code (object==null ? elem==null : object.equals(elem)}.
     * <p/>
     * The default implementation iterates over this {@code Collection} and tests for each element {@code e} returned by
     * the iterator, whether {@code e} is equal to the given object. If {@code object != null} then this test is
     * performed using {@code object.equals(e)}, otherwise using {@code object == null}. If an element equal to the
     * given object is found, then the {@code remove} method is called on the iterator and {@code true} is returned,
     * {@code false} otherwise. If the iterator does not support removing elements, an {@code
     * UnsupportedOperationException} is thrown.
     *
     * @param object the object to remove.
     * @return {@code true} if this {@code Collection} is modified, {@code false} otherwise.
     * @throws UnsupportedOperationException if removing from this {@code Collection} is not supported.
     * @throws ClassCastException            if the object passed is not of the correct type.
     * @throws NullPointerException          if {@code object} is {@code null} and this {@code Collection} doesn't
     *                                       support {@code null} elements.
     */
    public boolean remove(E object) {
        Iterator<E> it = iterator();
        if (object != null) {
            while (it.hasNext()) {
                if (object.equals(it.next())) {
                    it.remove();
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes all occurrences in this {@code Collection} of each object in the specified {@code Collection} (optional).
     * After this method returns none of the elements in the passed {@code Collection} can be found in this {@code
     * Collection} anymore.
     * <p/>
     * The default implementation iterates over this {@code Collection} and tests for each element {@code e} returned by
     * the iterator, whether it is contained in the specified {@code Collection}. If this test is positive, then the
     * {@code remove} method is called on the iterator. If the iterator does not support removing elements, an {@code
     * UnsupportedOperationException} is thrown.
     *
     * @param collection the collection of objects to remove.
     * @return {@code true} if this {@code Collection} is modified, {@code false} otherwise.
     * @throws UnsupportedOperationException if removing from this {@code Collection} is not supported.
     * @throws ClassCastException            if one or more elements of {@code collection} isn't of the correct type.
     * @throws NullPointerException          if {@code collection} contains at least one {@code null} element and this
     *                                       {@code Collection} doesn't support {@code null} elements.
     * @throws NullPointerException          if {@code collection} is {@code null}.
     */
    public boolean removeAll(Collection<E> collection) {
        boolean result = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (collection.contains(it.next())) {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    /**
     * Removes all objects from this {@code Collection} that are not also found in the {@code Collection} passed
     * (optional). After this method returns this {@code Collection} will only contain elements that also can be found
     * in the {@code Collection} passed to this method.
     * <p/>
     * The default implementation iterates over this {@code Collection} and tests for each element {@code e} returned by
     * the iterator, whether it is contained in the specified {@code Collection}. If this test is negative, then the
     * {@code remove} method is called on the iterator. If the iterator does not support removing elements, an {@code
     * UnsupportedOperationException} is thrown.
     *
     * @param collection the collection of objects to retain.
     * @return {@code true} if this {@code Collection} is modified, {@code false} otherwise.
     * @throws UnsupportedOperationException if removing from this {@code Collection} is not supported.
     * @throws ClassCastException            if one or more elements of {@code collection} isn't of the correct type.
     * @throws NullPointerException          if {@code collection} contains at least one {@code null} element and this
     *                                       {@code Collection} doesn't support {@code null} elements.
     * @throws NullPointerException          if {@code collection} is {@code null}.
     */
    public boolean retainAll(Collection<E> collection) {
        boolean result = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    /**
     * Returns a count of how many objects this {@code Collection} contains.
     *
     * @return how many objects this {@code Collection} contains, or {@code Integer.MAX_VALUE} if there are more than
     * {@code Integer.MAX_VALUE} elements in this {@code Collection}.
     */
    public abstract int size();

    /**
     * Returns a new array containing all elements contained in this {@code Collection}.
     * <p/>
     * If the implementation has ordered elements it will return the element array in the same order as an iterator
     * would return them.
     * <p/>
     * The array returned does not reflect any changes of the {@code Collection}. A new array is created even if the
     * underlying data structure is already an array.
     *
     * @return an array of the elements from this {@code Collection}.
     */
    public Object[] toArray() {
        int size = size(), index = 0;
        Iterator<E> it = iterator();
        Object[] array = new Object[size];
        while (index < size) {
            array[index++] = it.next();
        }
        return array;
    }

    /**
     * Copies all elements contained in this {@code Collection} to the specified array.   Unlike the regular java.lang
     * version of this method, here the array is never allocated & returned but instead a big enough array must be
     * passed in.   If the specified array isn't big enough to hold all elements, an exception is thrown.   If it's
     * bigger than needed, the array element following the {@code Collection} elements is set to null.
     * <p/>
     * Changes to the standard java.lang version of this method avoid the need for reflection and make it a little
     * simpler.   Typical usage would be to just allocate an array of exactly the right length, with size() elements.
     *
     * @param contents the array.
     */
    public <T> void toArray(T[] contents) {
        if (contents.length < size())
            throw new ProgrammerError(
                    "Array only has length {}, which isn't big enough to hold the {} elements in the collection",
                    contents.length, size());

        int index = 0;
        for (E entry : this) {
            contents[index++] = (T) entry;
        }
        if (index < contents.length) {
            contents[index] = null;
        }
    }

    /**
     * Returns the string representation of this {@code Collection}. The presentation has a specific format. It is
     * enclosed by square brackets ("[]"). Elements are separated by ', ' (comma and space).
     *
     * @return the string representation of this {@code Collection}.
     */
    @Override public String toString() {
        if (isEmpty()) {
            return "[]"; //$NON-NLS-1$
        }

        StringBuilder buffer = new StringBuilder(size() * 16);
        buffer.append('[');
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next != this) {
                buffer.append(next);
            } else {
                buffer.append("(this Collection)"); //$NON-NLS-1$
            }
            if (it.hasNext()) {
                buffer.append(", "); //$NON-NLS-1$
            }
        }
        buffer.append(']');
        return buffer.toString();
    }
}
