/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jsimple.util;

/**
 * Changes from the java.lang version:
 * Removed subList method.   It's rarely used (in my experience) and eliminated a fair amount of code/complexity.
 * <p/>
 * <p/>
 * {@code AbstractList} is an abstract implementation of the {@code List} interface, optimized
 * for a backing store which supports random access. This implementation does
 * not support adding or replacing. A subclass must implement the abstract
 * methods {@code get()} and {@code size()}, and to create a
 * modifiable {@code List} it's necessary to override the {@code add()} method that
 * currently throws an {@code UnsupportedOperationException}.
 *
 * @since 1.2
 */
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
    /**
     * A counter for changes to the list.
     */
    protected transient int modCount;

    /**
     * Constructs a new instance of this AbstractList.
     */
    protected AbstractList() {
        super();
    }

    /**
     * Inserts the specified object into this List at the specified location.
     * The object is inserted before any previous element at the specified
     * location. If the location is equal to the size of this List, the object
     * is added at the end.
     * <p/>
     * Concrete implementations that would like to support the add functionality
     * must override this method.
     *
     * @param location the index at which to insert.
     * @param object   the object to add.
     * @throws UnsupportedOperationException if adding to this List is not supported.
     * @throws ClassCastException            if the class of the object is inappropriate for this
     *                                       List
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
     * @throws ClassCastException            if the class of the object is inappropriate for this
     *                                       List
     * @throws IllegalArgumentException      if the object cannot be added to this List
     */
    @Override
    public boolean add(E object) {
        add(size(), object);
        return true;
    }

    /**
     * Inserts the objects in the specified Collection at the specified location
     * in this List. The objects are added in the order they are returned from
     * the collection's iterator.
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
     * By default, equals isn't supported for lists or other collections, though subclasses can choose to override this
     * behavior if they want.   Instead developers should call some other, more specific, method, checking for the exact
     * kind of equality they want.
     * <p/>
     * As background:  This behavior differs from the standard java.util.AbstractList.equals method, where two lists are
     * considered equal if the have the same number of elements and those elements are equals, according to equals,
     * in the same order.   That's so-called deep equality.   Whereas C#, on the other hand, just does reference equality
     * by default for lists, saying two lists are equal if they reference the same list.
     * <p/>
     * Anyway, there are two main reasons we differ from standard Java and don't support equals here or for other collection
     * methods:
     * <p/>
     * (a) The desired semantics are ambiguous (deep, shallow, or reference equality?  should order matter?  list type matter?).
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
    @Override public boolean equals(Object object) {
        throw new ProgrammerError("equals isn't supported by default for collections;  use == for reference equality or implement your own method for other types of equality");

        /*
        if (this == object) {
            return true;
        }

        if (object instanceof List) {
            List<?> list = (List<?>) object;
            if (list.size() != size()) {
                return false;
            }

            Iterator<E> it1 = iterator();
            Iterator<?> it2 = list.iterator();
            while (it1.hasNext()) {
                Object e1 = it1.next(), e2 = it2.next();
                if (!(e1 == null ? e2 == null : e1.equals(e2))) {
                    return false;
                }
            }
            return true;
        }
        return false;
        */
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
     * Returns the hash code of this list. The hash code is calculated by taking
     * each element's hashcode into account.
     *
     * @return the hash code.
     * @see #equals
     * @see List#hashCode()
     */
    @Override public int hashCode() {
        int result = 1;
        for (E object : this) {
            result = (31 * result) + (object == null ? 0 : object.hashCode());
        }
        return result;
    }
}
