using System;
using System.Collections;
using System.Collections.Generic;

namespace jsimple.util
{
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
    public class PlatformUtils : PlatformUtilsBase
    {
        /// <summary>
        ///     Get the number of milliseconds since Jan 1, 1970, UTC time.  That's also known as epoch time.  It's the time unit
        ///     we generally use in JSimple.
        /// </summary>
        /// <returns> number of milliseconds since 1/1/1970 UTC/GMT </returns>
        private static readonly System.DateTime Jan1st1970 = new System.DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);

        public static long CurrentTimeMillis
        {
            get { return (long) (System.DateTime.UtcNow - Jan1st1970).TotalMilliseconds; }
        }

        /// <summary>
        ///     Get the number of milliseconds since Jan 1, 1970, UTC time for dateTime, thus converting to the time unit we
        ///     we generally use in JSimple.
        /// </summary>
        /// <returns> number of milliseconds since 1/1/1970 UTC/GMT for dataTimeOffset </returns>
        public static long toMillisFromDateTime(System.DateTime dateTime)
        {
            return (long)(dateTime - Jan1st1970).TotalMilliseconds;
        }

        /// <summary>
        ///     Get the number of milliseconds since Jan 1, 1970, UTC time for dateTimeOffset, thus converting to the time unit we
        ///     we generally use in JSimple.
        /// </summary>
        /// <returns> number of milliseconds since 1/1/1970 UTC/GMT for dataTimeOffset </returns>
        public static long toMillisFromDateTimeOffset(DateTimeOffset dateTimeOffset)
        {
            return (long) (dateTimeOffset.UtcDateTime - Jan1st1970).TotalMilliseconds;
        }

        /// <summary>
        ///     Convert number of milliseconds since Jan 1, 1970, UTC time (the time unit generally used in JSimple) to a DateTime.
        /// </summary>
        /// <returns> DateTime corresponding to the date/time in millis </returns>
        public static System.DateTime toDotNetDateTimeFromMillis(long millis)
        {
            return Jan1st1970.AddMilliseconds(millis);
        }

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

        /// <summary>
        ///     Get the default line separator (typically newline or carriage return/newline) for the platform.
        /// </summary>
        /// <returns> default line separator for the platform </returns>
        public static String LineSeparator
        {
            get { return "\r\n"; }
        }
    }
}