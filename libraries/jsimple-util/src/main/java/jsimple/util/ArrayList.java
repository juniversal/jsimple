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

/**
 * ArrayList is an implementation of {@link List}, backed by an array. All optional operations adding, removing, and
 * replacing are supported. The elements can be any objects.
 * <p/>
 * Changes from the java.util version:
 * <p/>
 * Does not support clone; use constructor taking a Collection argument instead (which is more flexible and type safe)
 */
public final class ArrayList<E> extends List<E> {
    /**
     * A counter for changes to the list.
     */
    protected transient int modCount;

    private transient int firstIndex;
    private transient int itemCount;   // This member was named "size" in original Harmony source; renamed to not conflict with size() method
    private transient E[] array;

    private static class ArrayListIterator<E> extends Iterator<E> {
        private ArrayList<E> arrayList;
        private int numLeft;
        private int expectedModCount;
        private int lastPosition = -1;

        public ArrayListIterator(ArrayList<E> arrayList) {
            this.arrayList = arrayList;
            this.numLeft = arrayList.size();
            this.expectedModCount = arrayList.modCount;
        }

        @Override public boolean hasNext() {
            return numLeft > 0;
        }

        @Override public E next() {
            if (expectedModCount != arrayList.modCount) {
                throw createConcurrentModificationException();
            }

            int index = arrayList.itemCount - numLeft;

            if (index < 0 || index >= arrayList.itemCount) {
                throw new ProgrammerError("Index out of bounds: index: {}, size: {}", index, arrayList.itemCount);
            }
            E result = arrayList.array[arrayList.firstIndex + index];


            lastPosition = index;
            numLeft--;
            return result;
        }

        @Override public void remove() {
            if (lastPosition == -1) {
                throw new ProgrammerError("No current element to remove");
            }
            if (expectedModCount != arrayList.modCount) {
                throw createConcurrentModificationException();
            }


            if (lastPosition == arrayList.itemCount - numLeft) {
                numLeft--; // we're removing after a call to previous()
            }


            if (lastPosition < 0 || lastPosition >= arrayList.itemCount) {
                throw createConcurrentModificationException();
            }
            arrayList.remove(lastPosition);


            expectedModCount = arrayList.modCount;
            lastPosition = -1;
        }

        private ProgrammerError createConcurrentModificationException() {
            return new ProgrammerError("List modified outside of iterator: expected mod count is {}, actual mod count is {}", expectedModCount, arrayList.modCount);
        }
    }

    /**
     * Constructs a new instance of {@code ArrayList} with ten capacity.
     */
    public ArrayList() {
        this(10);
    }

    /**
     * Constructs a new instance of {@code ArrayList} with the specified capacity.
     *
     * @param capacity the initial capacity of this {@code ArrayList}.
     */
    public ArrayList(int capacity) {
        firstIndex = itemCount = 0;
        array = newElementArray(capacity);
    }

    /**
     * Constructs a new instance of {@code ArrayList} containing the elements of the specified collection. The initial
     * size of the {@code ArrayList} will be 10% larger than the size of the specified collection.
     * <p/>
     * Changes from the java.util version:  The constructor there supported covariance, taking a Collection&lt;? extends
     * E&gt; argument where the collection can be of a subclass of E.   This constructor only allows collections of
     * exact type E, but you can use the addAll method instead if you really need to copy a collection of a subclass.
     *
     * @param collection the collection of elements to add
     */
    public ArrayList(Collection<E> collection) {
        firstIndex = 0;
        Object[] objects = collection.toArray();
        itemCount = objects.length;

        // REVIEW: Created 2 array copies of the original collection here
        //         Could be better to use the collection iterator and
        //         copy once?
        array = newElementArray(itemCount + (itemCount / 10));
        SystemUtils.arraycopy(objects, 0, array, 0, itemCount);
        modCount = 1;
    }

    /**
     * Constructs an ArrayList, with the specified initial members.
     *
     * Changes from the java.util version:  The constructor is new, added as a convenience method to easily create
     * initialized lists.
     *
     * @param args elements to add initially to the list
     */
    public ArrayList(E... args) {
        this(args.length);
        for (E arg : args) {
            add(arg);
        }
    }

    @SuppressWarnings("unchecked")
    private E[] newElementArray(int size) {
        return (E[]) new Object[size];
    }

    /**
     * Inserts the specified object into this {@code ArrayList} at the specified location. The object is inserted before
     * any previous element at the specified location. If the location is equal to the size of this {@code ArrayList},
     * the object is added at the end.
     *
     * @param location the index at which to insert the object.
     * @param object   the object to add.
     * @throws IndexOutOfBoundsException when {@code location < 0 || > size()}
     */
    @Override
    public void add(int location, E object) {
        if (location < 0 || location > itemCount) {
            throw new ProgrammerError("Index out of bounds; index: {}, size: {}", location, itemCount);
        }
        if (location == 0) {
            if (firstIndex == 0) {
                growAtFront(1);
            }
            array[--firstIndex] = object;
        } else if (location == itemCount) {
            if (firstIndex + itemCount == array.length) {
                growAtEnd(1);
            }
            array[firstIndex + itemCount] = object;
        } else { // must be case: (0 < location && location < size)
            if (itemCount == array.length) {
                growForInsert(location, 1);
            } else if (firstIndex + itemCount == array.length
                    || (firstIndex > 0 && location < itemCount / 2)) {
                SystemUtils.arraycopy(array, firstIndex, array, --firstIndex, location);
            } else {
                int index = location + firstIndex;
                SystemUtils.arraycopy(array, index, array, index + 1, itemCount
                        - location);
            }
            array[location + firstIndex] = object;
        }

        itemCount++;
        modCount++;
    }

    /**
     * Adds the specified object at the end of this {@code ArrayList}.
     *
     * @param object the object to add.
     * @return always true
     */
    @Override
    public boolean add(E object) {
        if (firstIndex + itemCount == array.length) {
            growAtEnd(1);
        }
        array[firstIndex + itemCount] = object;
        itemCount++;
        modCount++;
        return true;
    }

    /**
     * Inserts the objects in the specified collection at the specified location in this List. The objects are added in
     * the order they are returned from the collection's iterator.
     *
     * @param location   the index at which to insert.
     * @param collection the collection of objects.
     * @return {@code true} if this {@code ArrayList} is modified, {@code false} otherwise.
     * @throws IndexOutOfBoundsException when {@code location < 0 || > size()}
     */
    @Override
    public boolean addAll(int location, Collection<? extends E> collection) {
        if (location < 0 || location > itemCount) {
            throw new ProgrammerError("Index out of bounds; index: {}, size: {}", location, itemCount);
        }

        // TODO: Rework this to use iterator instead; one reason is to avoid boxing

        Object[] dumparray = collection.toArray();
        int growSize = dumparray.length;
        // REVIEW: Why do this check here rather than check collection.size() earlier? RI behaviour?
        if (growSize == 0) {
            return false;
        }

        if (location == 0) {
            growAtFront(growSize);
            firstIndex -= growSize;
        } else if (location == itemCount) {
            if (firstIndex + itemCount > array.length - growSize) {
                growAtEnd(growSize);
            }
        } else { // must be case: (0 < location && location < size)
            if (array.length - itemCount < growSize) {
                growForInsert(location, growSize);
            } else if (firstIndex + itemCount > array.length - growSize
                    || (firstIndex > 0 && location < itemCount / 2)) {
                int newFirst = firstIndex - growSize;
                if (newFirst < 0) {
                    int index = location + firstIndex;
                    SystemUtils.arraycopy(array, index, array, index - newFirst,
                            itemCount - location);
                    newFirst = 0;
                }
                SystemUtils.arraycopy(array, firstIndex, array, newFirst, location);
                firstIndex = newFirst;
            } else {
                int index = location + firstIndex;
                SystemUtils.arraycopy(array, index, array, index + growSize, itemCount
                        - location);
            }
        }

        SystemUtils.arraycopy(dumparray, 0, this.array, location + firstIndex, growSize);
        itemCount += growSize;
        modCount++;
        return true;
    }

    /**
     * Adds the objects in the specified collection to this {@code ArrayList}.
     *
     * @param collection the collection of objects.
     * @return {@code true} if this {@code ArrayList} is modified, {@code false} otherwise.
     */
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        Object[] dumpArray = collection.toArray();
        if (dumpArray.length == 0) {
            return false;
        }
        if (dumpArray.length > array.length - (firstIndex + itemCount)) {
            growAtEnd(dumpArray.length);
        }
        SystemUtils.arraycopy(dumpArray, 0, this.array, firstIndex + itemCount, dumpArray.length);
        itemCount += dumpArray.length;
        modCount++;
        return true;
    }

    /**
     * Removes all elements from this {@code ArrayList}, leaving it empty.
     *
     * @see #isEmpty
     * @see #size
     */
    @Override public void clear() {
        if (itemCount != 0) {
            // REVIEW: Should we use Arrays.fill() instead of just
            //         allocating a new array?  Should we use the same
            //         sized array?
            fillWithDefault(firstIndex, firstIndex + itemCount);
            // REVIEW: Should the indexes point into the middle of the
            //         array rather than 0?
            firstIndex = itemCount = 0;
            modCount++;
        }
    }

    /**
     * Searches this {@code ArrayList} for the specified object.
     *
     * @param object the object to search for.
     * @return {@code true} if {@code object} is an element of this {@code ArrayList}, {@code false} otherwise
     */
    @Override
    public boolean contains(E object) {
        int lastIndex = firstIndex + itemCount;
        if (object != null) {
            for (int i = firstIndex; i < lastIndex; i++) {
                if (object.equals(array[i])) {
                    return true;
                }
            }
        } else {
            for (int i = firstIndex; i < lastIndex; i++) {
                if (array[i] == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Ensures that after this operation the {@code ArrayList} can hold the specified number of elements without further
     * growing.
     *
     * @param minimumCapacity the minimum capacity asked for.
     */
    public void ensureCapacity(int minimumCapacity) {
        int required = minimumCapacity - array.length;
        if (required > 0) {
            // REVIEW: Why do we check the firstIndex first? Growing
            //         the end makes more sense
            if (firstIndex > 0) {
                growAtFront(required);
            } else {
                growAtEnd(required);
            }
        }
    }

    @Override
    public E get(int location) {
        if (location < 0 || location >= itemCount) {
            throw new ProgrammerError("Index out of bounds; index: {}, size: {}", location, itemCount);
        }
        return array[firstIndex + location];
    }

    private void growAtEnd(int required) {
        if (array.length - itemCount >= required) {
            // REVIEW: as growAtEnd, why not move size == 0 out as special case
            if (itemCount != 0) {
                SystemUtils.arraycopy(array, firstIndex, array, 0, itemCount);
                int start = itemCount < firstIndex ? firstIndex : itemCount;
                // REVIEW: I think we null too much array.length should be lastIndex ?
                fillWithDefault(start, array.length);
            }
            firstIndex = 0;
        } else {
            // REVIEW: If size is 0?
            //         Does size/2 seems a little high!
            int increment = itemCount / 2;
            if (required > increment) {
                increment = required;
            }
            if (increment < 12) {
                increment = 12;
            }
            E[] newArray = newElementArray(itemCount + increment);
            if (itemCount != 0) {
                SystemUtils.arraycopy(array, firstIndex, newArray, 0, itemCount);
                firstIndex = 0;
            }
            array = newArray;
        }
    }

    private void growAtFront(int required) {
        if (array.length - itemCount >= required) {
            int newFirst = array.length - itemCount;
            // REVIEW: as growAtEnd, why not move size == 0 out as
            //         special case
            if (itemCount != 0) {
                SystemUtils.arraycopy(array, firstIndex, array, newFirst, itemCount);
                int lastIndex = firstIndex + itemCount;
                int length = lastIndex > newFirst ? newFirst : lastIndex;
                fillWithDefault(firstIndex, length);
            }
            firstIndex = newFirst;
        } else {
            int increment = itemCount / 2;
            if (required > increment) {
                increment = required;
            }
            if (increment < 12) {
                increment = 12;
            }
            E[] newArray = newElementArray(itemCount + increment);
            if (itemCount != 0) {
                SystemUtils.arraycopy(array, firstIndex, newArray, increment, itemCount);
            }
            firstIndex = newArray.length - itemCount;
            array = newArray;
        }
    }

    private void growForInsert(int location, int required) {
        // REVIEW: we grow too quickly because we are called with the
        //         size of the new collection to add without taking in
        //         to account the free space we already have
        int increment = itemCount / 2;
        if (required > increment) {
            increment = required;
        }
        if (increment < 12) {
            increment = 12;
        }
        E[] newArray = newElementArray(itemCount + increment);
        // REVIEW: biased towards leaving space at the beginning?
        //         perhaps newFirst should be (increment-required)/2?
        int newFirst = increment - required;
        // Copy elements after location to the new array skipping inserted
        // elements
        SystemUtils.arraycopy(array, location + firstIndex, newArray, newFirst
                + location + required, itemCount - location);
        // Copy elements before location to the new array from firstIndex
        SystemUtils.arraycopy(array, firstIndex, newArray, newFirst, location);
        firstIndex = newFirst;
        array = newArray;
    }

    @Override public int indexOf(E object) {
        // REVIEW: should contains call this method?
        int lastIndex = firstIndex + itemCount;
        if (object != null) {
            for (int i = firstIndex; i < lastIndex; i++) {
                if (object.equals(array[i])) {
                    return i - firstIndex;
                }
            }
        } else {
            for (int i = firstIndex; i < lastIndex; i++) {
                if (array[i] == null) {
                    return i - firstIndex;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return itemCount == 0;
    }

    @Override
    public int lastIndexOf(E object) {
        int lastIndex = firstIndex + itemCount;
        if (object != null) {
            for (int i = lastIndex - 1; i >= firstIndex; i--) {
                if (object.equals(array[i])) {
                    return i - firstIndex;
                }
            }
        } else {
            for (int i = lastIndex - 1; i >= firstIndex; i--) {
                if (array[i] == null) {
                    return i - firstIndex;
                }
            }
        }
        return -1;
    }

    /**
     * Returns an iterator on the elements of this list. The elements are iterated in the same order as they occur in
     * the list.
     *
     * @return an iterator on the elements of this list.
     * @see Iterator
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayListIterator<E>(this);
    }

    /**
     * Removes the object at the specified location from this list.
     *
     * @param location the index of the object to remove.
     * @return the removed object.
     * @throws IndexOutOfBoundsException when {@code location < 0 || >= size()}
     */
    @Override
    public E remove(int location) {
        E result;
        if (location < 0 || location >= itemCount) {
            throw new ProgrammerError("Index out of bounds; index: {}, size: {}", location, itemCount);
        }
        if (location == 0) {
            result = array[firstIndex];
            array[firstIndex++] = SystemUtils.<E>defaultValue();
        } else if (location == itemCount - 1) {
            int lastIndex = firstIndex + itemCount - 1;
            result = array[lastIndex];
            array[lastIndex] = SystemUtils.<E>defaultValue();
        } else {
            int elementIndex = firstIndex + location;
            result = array[elementIndex];
            if (location < itemCount / 2) {
                SystemUtils.arraycopy(array, firstIndex, array, firstIndex + 1, location);
                array[firstIndex++] = SystemUtils.<E>defaultValue();
            } else {
                SystemUtils.arraycopy(array, elementIndex + 1, array, elementIndex, itemCount - location - 1);
                array[firstIndex + itemCount - 1] = SystemUtils.<E>defaultValue();
            }
        }
        itemCount--;

        // REVIEW: we can move this to the first if case since it can only occur when size==1
        if (itemCount == 0) {
            firstIndex = 0;
        }

        modCount++;
        return result;
    }

    @Override public boolean remove(E object) {
        int location = indexOf(object);
        if (location >= 0) {
            remove(location);
            return true;
        }
        return false;
    }

    /**
     * Replaces the element at the specified location in this {@code ArrayList} with the specified object.
     *
     * @param location the index at which to put the specified object.
     * @param object   the object to add.
     * @return the previous element at the index.
     * @throws IndexOutOfBoundsException when {@code location < 0 || >= size()}
     */
    @Override
    public E set(int location, E object) {
        if (location < 0 || location >= itemCount) {
            throw new ProgrammerError("Index out of bounds; index: {}, size: {}", location, itemCount);
        }
        E result = array[firstIndex + location];
        array[firstIndex + location] = object;
        return result;
    }

    /**
     * Returns the number of elements in this {@code ArrayList}.
     *
     * @return the number of elements in this {@code ArrayList}.
     */
    @Override
    public int size() {
        return itemCount;
    }

    /**
     * Returns a new array containing all elements contained in this {@code ArrayList}.
     *
     * @return an array of the elements from this {@code ArrayList}
     */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[itemCount];
        SystemUtils.arraycopy(array, firstIndex, result, 0, itemCount);
        return result;
    }

    /**
     * Returns an array containing all elements contained in this {@code ArrayList}. If the specified array is large
     * enough to hold the elements, the specified array is used, otherwise an array of the same type is created. If the
     * specified array is used and is larger than this {@code ArrayList}, the array element following the collection
     * elements is set to null.
     *
     * @param contents the array.
     * @throws ArrayStoreException when the type of an element in this {@code ArrayList} cannot be stored in the type of
     *                             the specified array.
     */
    @Override public void toArray(E[] contents) {
        if (itemCount > contents.length)
            throw new ProgrammerError(
                    "Array only has length {}, which isn't big enough to hold the {} elements in the collection",
                    contents.length, size());

        SystemUtils.arraycopy(array, firstIndex, contents, 0, itemCount);
        if (itemCount < contents.length) {
            // REVIEW: do we use this incorrectly - i.e. do we null the rest out?
            contents[itemCount] = SystemUtils.<E>defaultValue();
        }
    }

    public void sortInPlace(Comparator<E> comparator) {
        Arrays.sort(array, firstIndex, firstIndex + itemCount, comparator);
        modCount++;
    }

    /**
     * Sets the capacity of this {@code ArrayList} to be the same as the current size.
     *
     * @see #size
     */
    public void trimToSize() {
        E[] newArray = newElementArray(itemCount);
        SystemUtils.arraycopy(array, firstIndex, newArray, 0, itemCount);
        array = newArray;
        firstIndex = 0;
        modCount = 0;
    }

    private void fillWithDefault(int fromIndex, int toIndex) {
        E defaultValue = SystemUtils.<E>defaultValue();
        for (int i = fromIndex; i < toIndex; i++) {
            array[i] = defaultValue;
        }
    }
}
