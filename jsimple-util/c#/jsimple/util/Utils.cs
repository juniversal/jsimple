using System.Collections.Generic;

namespace jsimple.util {



    /// <summary>
    /// Created with IntelliJ IDEA.
    /// 
    /// @author Bret Johnson
    /// @since 1/4/13 2:15 AM
    /// </summary>
    public class Utils {
        /// <summary>
        /// Return an array of bytes containing as elements this method's parameters.  Parameters are ints but they should be
        /// in a legal range for a byte (technically -128 to 127, but treating the range like 0-255 also works as expected).
        /// <p/>
        /// This method exists mostly to avoid casting problems populating a byte[] literals inline--casting issues caused by
        /// integer literals always being ints not bytes in Java (and thus requiring a byte cast--compile issue #1) and
        /// translated C# always treating hex literals as unsigned and not allowing ones > 127 to be cast to (signed) bytes
        /// (compile issue #2). Anyway, that's all avoided just by passing in ints here.
        /// </summary>
        /// <param name="bytes"> 0 or more bytes to convert into an array of bytes </param>
        /// <returns> array of bytes </returns>
        public static sbyte[] byteArrayFromBytes(params int[] bytes) {
            int length = bytes.Length;

            sbyte[] byteArray = new sbyte[length];
            for (int i = 0; i < length; i++)
                byteArray[i] = (sbyte) bytes[i];

            return byteArray;
        }

        public static int compare(long v1, long v2) {
            if (v1 < v2)
                return -1;
            else if (v1 == v2)
                return 0;
            else
                return 1;
        }

        public static long min(long v1, long v2) {
            if (v1 < v2)
                return v1;
            else
                return v2;
        }

        public static long max(long v1, long v2) {
            if (v1 > v2)
                return v1;
            else
                return v2;
        }

        /// <summary>
        /// Return true if the two objects are equal, according to their isEquals method.  This method allows the objects to
        /// be null, returning true if both are null & false if just one is null.
        /// </summary>
        /// <param name="o1"> object 1 </param>
        /// <param name="o2"> object 2 </param>
        /// <returns> true if both objects are equal or both are null, false otherwise </returns>
        public static bool equal(object o1, object o2) {
            if (o1 == o2)
                return true;
            if (o1 == null || o2 == null)
                return false;
            return o1.Equals(o2);
        }

        /// <summary>
        /// See if two ArrayLists are the same.  They are considered the same if their elements are all equal, according to
        /// the "equals" method.  This method can be used to overcome an incompatibility between Java and C# in the equals
        /// method for lists; Java checks for "deep" equality (like this method) while C# normally just checks if the lists
        /// reference the same object.  Using this method instead provides deep equality semantics for both.
        /// </summary>
        /// <param name="list1"> 1st ArrayList </param>
        /// <param name="list2"> 2nd ArrayList </param>
        /// <returns> true if the lists are equal, checking member by member equality, and false if not </returns>
        public static bool arrayListsEqual<T1, T2>(List<T1> list1, List<T2> list2) {
            int size = list1.Count;
            if (size != list2.Count)
                return false;

            for (int i = 0; i < size; i++) {
                object object1 = list1[i];
                object object2 = list2[i];

                if (object1 == null) {
                    if (object2 != null)
                        return false;
                }
                else {
                    if (!object1.Equals(object2))
                        return false;
                }
            }

            return true;
        }

        /// <summary>
        /// Get the "deep" hash code for an ArrayList, by combining hash codes of elements in the list.  This method can be
        /// used to overcome an incompatibility between Java and C# in the hashCode method for lists; Java checks for "deep"
        /// hash code (like this method) while C# normally just checks computes hash code based on object identity.  Using
        /// this method instead provides deep hash code semantics for both.  If you use this, you'll also want to use
        /// arrayListsEqual for equality, as the semantics of equality / hashCode have to go together (rule being if two
        /// objects equal, they MUST have the same hash code, which is true for both Java & C#).
        /// </summary>
        /// <param name="list"> list to compute hashCode of </param>
        /// <returns> list hash code, based on list elements </returns>
        public static int arrayListHashCode<T1>(List<T1> list) {
            int result = 1;

            // This source was copied from the Harmony implementation of list hash code
            int size = list.Count;
            for (int i = 0; i < size; i++) {
                object @object = list[i];
                result = (31 * result) + (@object == null ? 0 : @object.GetHashCode());
            }
            return result;
        }
    }

}