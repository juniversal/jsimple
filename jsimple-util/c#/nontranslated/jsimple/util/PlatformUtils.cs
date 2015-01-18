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
 */

using System;
using System.Collections;
using System.Collections.Generic;
using System.Threading;

namespace jsimple.util {
    /// <summary>
    ///     This class is used to wrap one off utility methods that need to be implemented in a platform dependent way.  Methods
    ///     here are static & not actually defined in the base class--just a template comment is given here, and the method
    ///     should actually be implemented in the platform dependent PlatformUtils subclass.  Callers should call, for example,
    ///     with the syntax PlatformUtils.stringGetChars().
    ///     <p />
    ///     By using the above scheme, calls are static & most efficient, there's no need for extra infrastructure around a
    ///     factory class, and perhaps most importantly if the platform doesn't implement a method which is actually needed by
    ///     some code it uses, that problem is caught at compile time not run time.
    ///     @author Bret Johnson
    ///     @since 10/21/12 3:26 PM
    /// </summary>
    public class PlatformUtils : PlatformUtilsBase {
        /// <summary>
        ///     Get the number of milliseconds since Jan 1, 1970, UTC time.  That's also known as epoch time.  It's the time unit
        ///     we generally use in JSimple.
        /// </summary>
        /// <returns> number of milliseconds since 1/1/1970 UTC/GMT </returns>
        private static readonly System.DateTime Jan1st1970 = new System.DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);

        public static long platformGetCurrentTimeMillis() {
            return (long)(System.DateTime.UtcNow - Jan1st1970).TotalMilliseconds;
        }

        /// <summary>
        ///     Get the number of milliseconds since Jan 1, 1970, UTC time for dateTime, thus converting to the time unit we
        ///     we generally use in JSimple.
        /// </summary>
        /// <returns> number of milliseconds since 1/1/1970 UTC/GMT for dataTimeOffset </returns>
        public static long toMillisFromDateTime(System.DateTime dateTime) {
            return (long)(dateTime - Jan1st1970).TotalMilliseconds;
        }

        /// <summary>
        ///     Get the number of milliseconds since Jan 1, 1970, UTC time for dateTimeOffset, thus converting to the time unit we
        ///     we generally use in JSimple.
        /// </summary>
        /// <returns> number of milliseconds since 1/1/1970 UTC/GMT for dataTimeOffset </returns>
        public static long toMillisFromDateTimeOffset(DateTimeOffset dateTimeOffset) {
            return (long)(dateTimeOffset.UtcDateTime - Jan1st1970).TotalMilliseconds;
        }

        /// <summary>
        ///     Convert number of milliseconds since Jan 1, 1970, UTC time (the time unit generally used in JSimple) to a DateTime.
        /// </summary>
        /// <returns> DateTime corresponding to the date/time in millis </returns>
        public static System.DateTime toDotNetDateTimeFromMillis(long millis) {
            return Jan1st1970.AddMilliseconds(millis);
        }

        /*
                /// <summary>
                ///     Sort the elements of the list in their natural order (that is, as specified by the Comparable interface they
                ///     implement).  The sortList isn't guaranteed to be stable (it actually is in the Java version but not in the C# version).
                /// </summary>
                /// <param name="list"> input list </param>
                public static void sortList<T>(List<T> list) where T : IComparable<T>
                {
                    list.Sort();
                }

                /// <summary>
                /// Sort the elements of the list based on the Comparator callback passed in.  The sort isn't guaranteed to be stable
                /// (it actually is in the Java version but not in the C# version).
                /// </summary>
                /// <param name="list"> input list </param>
                /// <param name="comparer"> list element comparer </param>
                public static void sortList<T>(List<T> list, IComparer<T> comparer)
                {
                    list.Sort(comparer);
                }
        */

        /// <summary>
        ///     Get the default line separator (typically newline or carriage return/newline) for the platform.
        /// </summary>
        /// <returns> default line separator for the platform </returns>
        public static String getLineSeparator() {
            return "\r\n";
        }

        public static String getExceptionDescription(Exception e) {
            return e.ToString();
        }

        public static void sleep(int sleepTimeInMilliseconds) {
            ManualResetEvent manualResetEvent = new ManualResetEvent(false);
            manualResetEvent.WaitOne(sleepTimeInMilliseconds);
        }

        /// <summary>
        /// Copy data from source byte array to destination byte array.
        /// </summary>
        /// <param name="src"> source array </param>
        /// <param name="srcPos"> starting position in source array </param>
        /// <param name="dest"> destination array </param>
        /// <param name="destPos"> position in destination array </param>
        /// <param name="length"> number of elements to copy </param>
        public static void copyBytes(sbyte[] src, int srcPos, sbyte[] dest, int destPos, int length) {
            Buffer.BlockCopy(src, srcPos, dest, destPos, length);
        }

        /// <summary>
        /// Copy data from source char array to destination char array.
        /// </summary>
        /// <param name="src"> source array </param>
        /// <param name="srcPos"> starting position in source array </param>
        /// <param name="dest"> destination array </param>
        /// <param name="destPos"> position in destination array </param>
        /// <param name="length"> number of elements to copy </param>
        public static void copyChars(char[] src, int srcPos, char[] dest, int destPos, int length) {
            Buffer.BlockCopy(src, srcPos, dest, destPos, length * sizeof(char));
        }

        /// <summary>
        /// Copy data from source char string to destination char array.
        /// </summary>
        /// <param name="src"> source array </param>
        /// <param name="srcPos"> starting position in source array </param>
        /// <param name="dest"> destination array </param>
        /// <param name="destPos"> position in destination array </param>
        /// <param name="length"> number of elements to copy </param>
        public static void copyChars(string src, int srcPos, char[] dest, int destPos, int length) {
            src.CopyTo(srcPos, dest, destPos, length);
        }

        public static void useSystemProxy() {
            // TODO: Do we need to ever do anything here?  Maybe not since using the OS proxy seems to be the default
        }

        public static E defaultValue<E>() {
            return default(E);
        }

        /// <summary>
        /// Copies the number of {@code length} elements of the Array {@code src} starting at the offset {@code srcPos} into the Array {@code dest} at the position {@code destPos}.
        /// </summary>
        /// <param name="src">the source array to copy the content</param>
        /// <param name="srcPos">the starting index of the content in {@code src}</param>
        /// <param name="dest">the destination array to copy the data into</param>
        /// <param name="destPos">the starting index for the copied content in {@code dest</param>
        /// <param name="length">the number of elements of the {@code array1} content they haveto be copied.</param>
        public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
            Array.Copy((Array)src, srcPos, (Array)dest, destPos, length);
        }

        /// <summary>
        /// Return true if the two objects are equal, according to their equals method.  For reference types, this method
        /// allows the objects to be null, returning true only if both are null.   And this method properly supports value
        /// types for languages that support them (e.g. in C#). 
        /// 
        /// </summary>
        /// <typeparam name="T">object type</typeparam>
        /// <param name="object1">object 1</param>
        /// <param name="object2">object 2</param>
        /// <returns></returns>
        public static bool equals<T>(T object1, T object2) {
            return EqualityComparer<T>.Default.Equals(object1, object2);
        }

        public static bool equalTo<T>(T object1, T object2) where T : Equatable<T> {
            if (isNullOrTypeDefault(object1))
                return isNullOrTypeDefault(object2);
            else return object1.equalTo(object2);
        }

        /**
         * Returns true if the object has the default value for the type.   The default is null for reference types.   For
         * languages that support value types (e.g. C# and C++), the default value is 0 for integers, etc.
         *
         * @param object object in question
         * @param <T>    type in question
         * @return true if object is the default value for the type otherwise
         */

        public static bool isNullOrTypeDefault<T>(T obj)
        {
            return EqualityComparer<T>.Default.Equals(obj, default(T));
        }
    }
}