/*
 * Copyright (c) 2012-2015, Microsoft Mobile
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
 */

package jsimple.util;

/**
 * Created by Bret on 5/19/2014.
 */
public class Lists {
    /**
     * See if two ArrayLists are the same.  They are considered the same if their elements are all equal, according to
     * the "equals" method.  This method can be used to overcome an incompatibility between Java and C# in the equals
     * method for lists; Java checks for "deep" equality (like this method) while C# normally just checks if the lists
     * reference the same object.  Using this method instead provides deep equality semantics for both.
     *
     * @param list1 1st ArrayList
     * @param list2 2nd ArrayList
     * @return true if the lists are equal, checking member by member equality, and false if not
     */
    public static boolean arrayListsEqual(ArrayList<?> list1, ArrayList<?> list2) {
        int size = list1.size();
        if (size != list2.size())
            return false;

        for (int i = 0; i < size; i++) {
            Object object1 = list1.get(i);
            Object object2 = list2.get(i);

            if (object1 == null) {
                if (object2 != null)
                    return false;
            } else {
                if (!object1.equals(object2))
                    return false;
            }
        }

        return true;
    }

    /**
     * Get the "deep" hash code for an ArrayList, by combining hash codes of elements in the list.  This method can be
     * used to overcome an incompatibility between Java and C# in the hashCode method for lists; Java checks for "deep"
     * hash code (like this method) while C# normally just checks computes hash code based on object identity.  Using
     * this method instead provides deep hash code semantics for both.  If you use this, you'll also want to use
     * arrayListsEqual for equality, as the semantics of equality / hashCode have to go together (rule being if two
     * objects equal, they MUST have the same hash code, which is true for both Java & C#).
     *
     * @param list list to compute hashCode of
     * @return list hash code, based on list elements
     */
    public static int arrayListHashCode(ArrayList<?> list) {
        int result = 1;

        // This source was copied from the Harmony implementation of list hash code
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Object object = list.get(i);
            result = (31 * result) + (object == null ? 0 : object.hashCode());
        }
        return result;
    }

    /**
     * Return a list of all distinct elements in the list, removing any duplicates.   Returned items are in the same
     * order as in the original list.   Note that the input list shouldn't contain any nulls--results are undefined if
     * it does.
     *
     * @param list list to remove duplicates from
     * @param <E> list element type
     * @return list with duplicates removed
     */
    public static <E> ArrayList<E> distinctElements(List<E> list) {
        ArrayList<E> newList = new ArrayList<E>();
        HashSet<E> hashSet = new HashSet<E>();

        for (E e : list) {
            if (!hashSet.contains(e)) {
                newList.add(e);
                hashSet.add(e);
            }
        }

        return newList;
    }
}
