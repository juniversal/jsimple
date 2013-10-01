package jsimple.util;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Bret Johnson
 * @since 1/4/13 2:15 AM
 */
public class Utils {
    /**
     * Return an array of bytes containing as elements this method's parameters.  Parameters are ints but they should be
     * in a legal range for a byte (technically -128 to 127, but treating the range like 0-255 also works as expected).
     * <p/>
     * This method exists mostly to avoid casting problems populating a byte[] literals inline--casting issues caused by
     * integer literals always being ints not bytes in Java (and thus requiring a byte cast--compile issue #1) and
     * translated C# always treating hex literals as unsigned and not allowing ones > 127 to be cast to (signed) bytes
     * (compile issue #2). Anyway, that's all avoided just by passing in ints here.
     *
     * @param bytes 0 or more bytes to convert into an array of bytes
     * @return array of bytes
     */
    public static byte[] byteArrayFromBytes(int... bytes) {
        int length = bytes.length;

        byte[] byteArray = new byte[length];
        for (int i = 0; i < length; i++)
            byteArray[i] = (byte) bytes[i];

        return byteArray;
    }

    public static int compare(long v1, long v2) {
        if (v1 < v2)
            return -1;
        else if (v1 == v2)
            return 0;
        else return 1;
    }

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
}
