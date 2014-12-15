using System;

namespace jsimple.lang
{
    public class System
    {
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
            */

        public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
        {
            Array.Copy((Array) src, srcPos, (Array) dest, destPos, length);
        }
    }
}
