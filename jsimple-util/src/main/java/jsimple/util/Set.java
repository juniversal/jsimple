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

/**
 * An AbstractSet is an abstract implementation of the Set interface. This implementation does not support adding. A
 * subclass must implement the abstract methods iterator() and size().
 *
 * @since 1.2
 */
public abstract class Set<E> extends Collection<E> {
    /**
     * Constructs a new instance of this AbstractSet.
     */
    protected Set() {
        super();
    }


/*
    */
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
 *//*

    public boolean equals(Object object);
*/

    /**
     * By default, equals isn't supported for sets or other collections, though subclasses can choose to override this
     * behavior if they want.   Instead developers should call some other, more specific, method, checking for the exact
     * kind of equality they want.
     * <p/>
     * As background:  This behavior differs from the standard java.util.AbstractSet.equals method, where two sets are
     * considered equal if the have the same number of elements and those elements are equals, according to equals.
     * That's so-called deep equality.   Whereas C#, on the other hand, just does reference equality by default for
     * sets, saying two are equal if their references are equals.
     * <p/>
     * There are two main reasons we differ from standard Java and don't support equals here for collection classes:
     * <p/>
     * (a) The desired semantics are ambiguous (deep, shallow, or reference equality?  set type matter?). Different
     * semantics are appropriate in different cases. (b) It's hard to implement the Java semantics and support generics,
     * with covariance, properly and have it work with Java to C#/Swift translation.  Limited C# and Swift support for
     * covariance, especially when casting from an Object, makes it all the harder. So best to just avoid all those
     * issues & make the developer do explicitly what they want, calling some other method.
     *
     * @param object the object to compare to this object.
     * @return {@code true} if the specified object is equal to this set, {@code false} otherwise; though by default
     * this method throws an exception and developers generally shouldn't use equals on collections
     */
    @Override public boolean equals(Object object) {
        throw new ProgrammerError("equals isn't supported by default for collections;  use == for reference equality or implement your own method for other types of equality");
        /*
        if (this == object) {
            return true;
        }
        if (object instanceof Set) {
            Set<?> s = (Set<?>) object;

            try {
                return size() == s.size() && containsAll(s);
            } catch (NullPointerException ignored) {
                return false;
            } catch (ClassCastException ignored) {
                return false;
            }
        }
        return false;
        */
    }

    /**
     * Returns the hash code for this set. Two set which are equal must return the same value.
     * <p/>
     * The default implementation calculates the hash code by adding each element's hash code.
     *
     * @return the hash code of this set.
     * @see #equals
     */
    @Override
    public int hashCode() {
        int result = 0;
        for (E next : this) {
            result += next == null ? 0 : next.hashCode();
        }
        return result;
    }

    /**
     * Removes all occurrences in this collection which are contained in the specified collection.
     *
     * @param collection the collection of objects to remove.
     * @return {@code true} if this collection was modified, {@code false} otherwise.
     */
    @Override public boolean removeAll(Collection<E> collection) {
        boolean result = false;
        if (size() <= collection.size()) {
            Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (collection.contains(it.next())) {
                    it.remove();
                    result = true;
                }
            }
        } else {
            for (E otherElmt : collection) {
                result = remove(otherElmt) || result;
            }
        }
        return result;
    }
}
