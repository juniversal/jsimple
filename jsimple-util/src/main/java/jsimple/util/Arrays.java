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
 * {@code Arrays} contains static methods which operate on arrays.
 */
public class Arrays {

    /* Specifies when to switch to insertion sort */
    private static final int SIMPLE_LENGTH = 7;

    private Arrays() {
        /* empty */
    }

    /**
     * Performs a binary search for the specified element in the specified sorted array using the Comparator to compare
     * elements.
     *
     * @param <T>        type of object
     * @param array      the sorted array to search
     * @param object     the element to find
     * @param comparator the Comparator
     * @return the non-negative index of the element, or a negative index which is the -index - 1 where the element
     * would be inserted
     */
    public static <T> int binarySearch(T[] array, T object, Comparator<T> comparator) {
        return binarySearch(array, 0, array.length, object, comparator);
    }

    /**
     * Performs a binary search for the specified element in a part of the specified sorted array using the Comparator
     * to compare elements.
     *
     * @param <T>        type of object
     * @param array      the sorted array to search
     * @param startIndex the inclusive start index
     * @param endIndex   the exclusive end index
     * @param object     the value element to find
     * @param comparator the Comparator
     * @return the non-negative index of the element, or a negative index which is the -index - 1 where the element
     * would be inserted
     * @throws ProgrammerError - if startIndex is bigger than endIndex
     * @throws ProgrammerError - if startIndex is smaller than zero or or endIndex is bigger than length of array
     */
    public static <T> int binarySearch(T[] array, int startIndex, int endIndex, T object, Comparator<T> comparator) {
        checkIndexForBinarySearch(array.length, startIndex, endIndex);

        int low = startIndex, mid = -1, high = endIndex - 1, result = 0;
        while (low <= high) {
            mid = (low + high) >>> 1;
            if ((result = comparator.compare(array[mid], object)) < 0) {
                low = mid + 1;
            } else if (result == 0) {
                return mid;
            } else {
                high = mid - 1;
            }
        }
        if (mid < 0) {
            int insertPoint = endIndex;
            for (int index = startIndex; index < endIndex; index++) {
                if (comparator.compare(object, array[index]) < 0) {
                    insertPoint = index;
                }
            }
            return -insertPoint - 1;
        }
        return -mid - (result >= 0 ? 1 : 2);
    }

    /**
     * Fills the array with the given value.
     *
     * @param length length of the array
     * @param start  start index
     * @param end    end index
     */
    private static void checkIndexForBinarySearch(int length, int start, int end) {
        if (start > end) {
            throw new ProgrammerError("Start index {} is after end index {}", start, end);
        }
        if (length < end || 0 > start) {
            throw new ProgrammerError("End {} is greater than array length {} or start {} is negative", end, length, start);
        }
    }

    /**
     * Fills the specified array with the specified element.
     *
     * @param array the array to fill.
     * @param value the element to fill with
     */
    public static <T> void fill(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            array[i] = value;
        }
    }

    /**
     * Fills the specified range in the array with the specified element.
     *
     * @param array the array to fill.
     * @param start the first index to fill.
     * @param end   the last + 1 index to fill.
     * @param value the element.
     * @throws ProgrammerError if {@code start > end}.
     * @throws ProgrammerError if {@code start < 0} or {@code end > array.length}.
     */
    public static <T> void fill(T[] array, int start, int end, T value) {
        checkBounds(array.length, start, end);
        for (int i = start; i < end; i++) {
            array[i] = value;
        }
    }

    /**
     * Returns a hash code based on the contents of the given array. If the array contains other arrays as its elements,
     * the hash code is based on their identities not their contents. So it is acceptable to invoke this method on an
     * array that contains itself as an element, either directly or indirectly.
     * <p/>
     * For any two arrays {@code a} and {@code b}, if {@code Arrays.equals(a, b)} returns {@code true}, it means that
     * the return value of {@code Arrays.hashCode(a)} equals {@code Arrays.hashCode(b)}.
     * <p/>
     * The value returned by this method is the same value as the method Arrays.asList(array).hashCode(). If the array
     * is {@code null}, the return value is 0.
     *
     * @param array the array whose hash code to compute.
     * @return the hash code for {@code array}.
     */
    public static <T> int hashCode(T[] array) {
        if (array == null) {
            return 0;
        }
        int hashCodeValue = 1;
        for (T element : array) {
            int elementHashCode;

            if (element == null) {
                elementHashCode = 0;
            } else {
                elementHashCode = element.hashCode();
            }
            hashCodeValue = 31 * hashCodeValue + elementHashCode;
        }
        return hashCodeValue;
    }

    /**
     * Compares the two arrays.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @return {@code true} if both arrays are {@code null} or if the arrays have the same length and the elements at
     * each index in the two arrays are equal according to {@code equals()}, {@code false} otherwise.
     */
    public static <T> boolean equals(T[] array1, T[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            T e1 = array1[i], e2 = array2[i];
            if (!(e1 == null ? e2 == null : e1.equals(e2))) {
                return false;
            }
        }
        return true;
    }

    private static void checkBounds(int arrLength, int start, int end) {
        if (start > end) {
            throw new ProgrammerError("Start index {} is greater than end index {})", start, end);
        }
        if (start < 0) {
            throw new ProgrammerError("Array index out of range: {}", start);
        }
        if (end > arrLength) {
            throw new ProgrammerError("Array index out of range: {}", end);
        }
    }

    /**
     * Performs a sort on the section of the array between the given indices using a mergesort with exponential search
     * algorithm (in which the merge is performed by exponential search). n*log(n) performance is guaranteed and in the
     * average case it will be faster then any mergesort in which the merge is performed by linear search.
     *
     * @param in    - the array for sorting.
     * @param out   - the result, sorted array.
     * @param start the start index
     * @param end   the end index + 1
     * @param c     - the comparator to determine the order of the array.
     */
    private static <T> void mergeSort(T[] in, T[] out, int start, int end, Comparator<T> c) {
        int len = end - start;
        // use insertion sort for small arrays
        if (len <= SIMPLE_LENGTH) {
            for (int index = start + 1; index < end; index++) {
                T current = out[index];
                T prev = out[index - 1];
                if (c.compare(prev, current) > 0) {
                    int j = index;
                    do {
                        out[j--] = prev;
                    } while (j > start
                            && (c.compare(prev = out[j - 1], current) > 0));
                    out[j] = current;
                }
            }
            return;
        }
        int med = (end + start) >>> 1;
        mergeSort(out, in, start, med, c);
        mergeSort(out, in, med, end, c);

        // merging

        // if arrays are already sorted - no merge
        if (c.compare(in[med - 1], in[med]) <= 0) {
            PlatformUtils.arraycopy(in, start, out, start, len);
            return;
        }
        int r = med, i = start;

        // use merging with exponential search
        do {
            T fromVal = in[start];
            T rVal = in[r];
            if (c.compare(fromVal, rVal) <= 0) {
                int l_1 = find(in, rVal, -1, start + 1, med - 1, c);
                int toCopy = l_1 - start + 1;
                PlatformUtils.arraycopy(in, start, out, i, toCopy);
                i += toCopy;
                out[i++] = rVal;
                r++;
                start = l_1 + 1;
            } else {
                int r_1 = find(in, fromVal, 0, r + 1, end - 1, c);
                int toCopy = r_1 - r + 1;
                PlatformUtils.arraycopy(in, r, out, i, toCopy);
                i += toCopy;
                out[i++] = fromVal;
                start++;
                r = r_1 + 1;
            }
        } while ((end - r) > 0 && (med - start) > 0);

        // copy rest of array
        if ((end - r) <= 0) {
            PlatformUtils.arraycopy(in, start, out, i, med - start);
        } else {
            PlatformUtils.arraycopy(in, r, out, i, end - r);
        }
    }

    /**
     * Finds the place of specified range of specified sorted array, where the element should be inserted for getting
     * sorted array. Uses exponential search algorithm.
     *
     * @param arr - the array with already sorted range
     * @param val - object to be inserted
     * @param l   - the start index
     * @param r   - the end index
     * @param bnd - possible values 0,-1. "-1" - val is located at index more then elements equals to val. "0" - val is
     *            located at index less then elements equals to val.
     * @param c   - the comparator used to compare Objects
     */
    private static <T> int find(T[] arr, T val, int bnd, int l, int r, Comparator<T> c) {
        int m = l;
        int d = 1;
        while (m <= r) {
            if (c.compare(val, arr[m]) > bnd) {
                l = m + 1;
            } else {
                r = m - 1;
                break;
            }
            m += d;
            d <<= 1;
        }
        while (l <= r) {
            m = (l + r) >>> 1;
            if (c.compare(val, arr[m]) > bnd) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return l - 1;
    }

    /**
     * Sorts the specified array range, from start (inclusive) to end (exclusive), using the specified {@code
     * Comparator}.
     *
     * @param array      the array to be sorted.
     * @param start      the start index to sort.
     * @param end        the last + 1 index to sort.
     * @param comparator the {@code Comparator}.
     * @throws ProgrammerError if {@code start > end}.
     * @throws ProgrammerError if {@code start < 0} or {@code end > array.length}.
     */
    public static <T> void sort(T[] array, int start, int end, Comparator<T> comparator) {
        checkBounds(array.length, start, end);
        sort(start, end, array, comparator);
    }

    private static <T> void sort(int start, int end, T[] array, Comparator<T> comparator) {
        int length = end - start;
        T[] out = (T[]) new Object[end];
        PlatformUtils.arraycopy(array, start, out, start, length);
        mergeSort(out, array, start, end, comparator);
    }

    /**
     * Sorts the specified array using the specified {@code Comparator}.
     *
     * @param array      the array to be sorted
     * @param comparator the {@code Comparator}
     */
    public static <T> void sort(T[] array, Comparator<T> comparator) {
        sort(0, array.length, array, comparator);
    }

    /**
     * Creates a {@code String} representation of the array passed. The result is surrounded by brackets ({@code
     * &quot;[]&quot;}), each element is converted to a {@code String} via the {@link String#valueOf(Object)} and
     * separated by {@code &quot;, &quot;}. If the array is {@code null}, then {@code &quot;null&quot;} is returned.
     *
     * @param array the array to convert.
     * @return the {@code String} representation of {@code array}.
     */
    public static <T> String toString(T[] array) {
        if (array == null) {
            return "null"; //$NON-NLS-1$
        }
        if (array.length == 0) {
            return "[]"; //$NON-NLS-1$
        }
        StringBuilder sb = new StringBuilder(2 + array.length * 5);
        sb.append('[');
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            sb.append(", "); //$NON-NLS-1$
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }

/**
 * Copies the number of {@code length} elements of the Array {@code src}
 * starting at the offset {@code srcPos} into the Array {@code dest} at
 * the position {@code destPos}.
 *
 * @param src
 *            the source array to copy the content.
 * @param srcPos
 *            the starting index of the content in {@code src}.
 * @param dest
 *            the destination array to copy the data into.
 * @param destPos
 *            the starting index for the copied content in {@code dest}.
 * @param length
 *            the number of elements of the {@code array1} content they have
 *            to be copied.
 *//*

    private static <T> void copy(T[] src, int srcPos, T[] dest, int destPos, int length) {
        if (srcPos >= 0 && destPos >= 0 && length >= 0 && length <= src.length - srcPos
                && length <= dest.length - destPos) {
            // Check if this is a forward or backwards arraycopy
            if (src != dest || srcPos > destPos || srcPos + length <= destPos) {
                for (int i = 0; i < length; ++i) {
                    dest[destPos + i] = src[srcPos + i];
                }
            } else {
                for (int i = length - 1; i >= 0; --i) {
                    dest[destPos + i] = src[srcPos + i];
                }
            }
        } else {
            throw new ProgrammerError("Passed arguments to Arrays.copy would go outside array bounds");
        }
    }
*/
}
