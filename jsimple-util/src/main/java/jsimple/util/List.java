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
 * A {@code List} is a collection which maintains an ordering for its elements. Every element in the {@code List} has an
 * index. Each element can thus be accessed by its index, with the first index being zero. Normally, {@code List}s allow
 * duplicate elements, as compared to Sets, where elements have to be unique.
 * <p/>
 * {@code AbstractList} is an abstract implementation of the {@code List} interface, optimized for a backing store which
 * supports random access. This implementation does not support adding or replacing. A subclass must implement the
 * abstract methods {@code get()} and {@code size()}, and to create a modifiable {@code List} it's necessary to override
 * the {@code add()} method that currently throws an {@code UnsupportedOperationException}.
 * <p/>
 * Changes from the java.lang version: Removed subList method.   It's rarely used (in my experience) and eliminated a
 * fair amount of code/complexity.
 *
 * @since 1.2
 */
public abstract class List<E> extends Collection<E> implements Equatable<List<E>> {
    /**
     * Constructs a new instance of this AbstractList.
     */
    protected List() {
        super();
    }

    /**
     * Inserts the specified object into this {@code List} at the specified location. The object is inserted before the
     * current element at the specified location. If the location is equal to the size of this {@code List}, the object
     * is added at the end. If the location is smaller than the size of this {@code List}, then all elements beyond the
     * specified location are moved by one position towards the end of the {@code List}.
     * <p/>
     * Concrete implementations that would like to support the add functionality must override this method.
     *
     * @param location the index at which to insert.
     * @param object   the object to add.
     * @throws UnsupportedOperationException if adding to this List is not supported.
     * @throws ClassCastException            if the class of the object is inappropriate for this List
     * @throws IllegalArgumentException      if the object cannot be added to this List
     * @throws IndexOutOfBoundsException     if {@code location < 0 || >= size()}
     */
    public void add(int location, E object) {
        throw new ProgrammerError("add at location is not supported for this kind of list");
    }

    /**
     * Adds the specified object at the end of this List.
     *
     * @param object the object to add
     * @return true
     * @throws UnsupportedOperationException if adding to this List is not supported
     * @throws ClassCastException            if the class of the object is inappropriate for this List
     * @throws IllegalArgumentException      if the object cannot be added to this List
     */
    @Override
    public boolean add(E object) {
        add(size(), object);
        return true;
    }

    /**
     * Inserts the objects in the specified Collection at the specified location in this List. The objects are added in
     * the order they are returned from the collection's iterator.
     *
     * @param location   the index at which to insert.
     * @param collection the Collection of objects
     * @return {@code true} if this List is modified, {@code false} otherwise.
     * @throws UnsupportedOperationException if adding to this list is not supported.
     * @throws ClassCastException            if the class of an object is inappropriate for this list.
     * @throws IllegalArgumentException      if an object cannot be added to this list.
     * @throws IndexOutOfBoundsException     if {@code location < 0 || > size()}
     */
    public boolean addAll(int location, Collection<? extends E> collection) {
        for (E item : collection) {
            add(location++, item);
        }
        return !collection.isEmpty();
    }

    /**
     * The default Java equals(Object) method isn't supported for JSimple lists.   Instead you should use
     * equals(List&lt;E&gt;).
     * <p/>
     * Background:  The reason for this is the the default Java version isn't typesafe and with casting from Object
     * (both for the list itself and its members) it's too hard to make everything work properly with translation to C#
     * / other languages that have reified (that is, "real", not type erased) generics and value types, with consistent
     * semantics around covariance and avoiding the performance hit of boxing for value types. Better to avoid all that
     * and go with the more modern, typesafe version of equals.   It can perform slightly better even in straight Java
     * too.
     *
     * @param object object to compare against
     * @return always throws an exception; don't use this method and use the typesafe equals(List&lt;E&gt;) equals
     * instead
     */
    @Override public boolean equals(Object object) {
        throw new ProgrammerError("equals(Object) isn't supported by default for lists; use equalTo(List<E>) instead as it's more type safe and performant");
    }

    /**
     * Two lists are considered equal if the both contain the same number of elements and the elements of both, when
     * traversed in order, are equal according to elements' equals() methods.
     *
     * @param otherList list to compare against
     * @return true if and only if the lists are equal
     */
    @Override public boolean equalTo(@Nullable List<E> otherList) {
        if (this == otherList)
            return true;

        if (otherList == null)
            return false;

        if (otherList.size() != size())
            return false;

        Iterator<E> it1 = iterator();
        Iterator<E> it2 = otherList.iterator();
        while (it1.hasNext()) {
            E e1 = it1.next();
            E e2 = it2.next();

            if (!PlatformUtils.equals(e1, e2))
                return false;
        }
        return true;
    }

    /**
     * Returns the element at the specified location in this list.
     *
     * @param location the index of the element to return.
     * @return the element at the specified index.
     * @throws IndexOutOfBoundsException if {@code location < 0 || >= size()}
     */
    public abstract E get(int location);

    /**
     * Returns the hash code of this list. The hash code is calculated by taking each element's hashcode into account.
     *
     * @return the hash code.
     * @see #equals
     * @see List#hashCode()
     */
    @Override public int hashCode() {
        int result = 1;
        for (E item : this) {
            result = (31 * result) + (item == null ? 0 : item.hashCode());
        }
        return result;
    }

    /**
     * Searches this {@code List} for the specified object and returns the index of the first occurrence.
     *
     * @param object the object to search for.
     * @return the index of the first occurrence of the object or -1 if the object was not found.
     */
    public abstract int indexOf(E object);

    /**
     * Searches this {@code List} for the specified object and returns the index of the last occurrence.
     *
     * @param object the object to search for.
     * @return the index of the last occurrence of the object, or -1 if the object was not found.
     */
    public abstract int lastIndexOf(E object);

    /**
     * Removes the object at the specified location from this {@code List}.
     *
     * @param location the index of the object to remove.
     * @return the removed object.
     * @throws UnsupportedOperationException if removing from this {@code List} is not supported.
     * @throws IndexOutOfBoundsException     if {@code location < 0 || >= size()}
     */
    public abstract E remove(int location);

    /**
     * Replaces the element at the specified location in this {@code List} with the specified object. This operation
     * does not change the size of the {@code List}.
     *
     * @param location the index at which to put the specified object.
     * @param object   the object to insert.
     * @return the previous element at the index.
     * @throws UnsupportedOperationException if replacing elements in this {@code List} is not supported.
     * @throws ClassCastException            if the class of an object is inappropriate for this {@code List}.
     * @throws IllegalArgumentException      if an object cannot be added to this {@code List}.
     * @throws IndexOutOfBoundsException     if {@code location < 0 || >= size()}
     */
    public abstract E set(int location, E object);

    /**
     * Returns an iterator on the elements of this {@code List}. The elements are
     * iterated in the same order as they occur in the {@code List}.
     *
     * @return an iterator on the elements of this {@code List}.
     * @see Iterator
     */
    //public abstract Iterator<E> iterator();
}
