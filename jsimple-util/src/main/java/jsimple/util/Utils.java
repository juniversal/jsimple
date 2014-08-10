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

package jsimple.util;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    public static int compare(double v1, double v2) {
        if (v1 < v2)
            return -1;
        else if (v1 == v2)
            return 0;
        else return 1;
    }

    public static long min(long v1, long v2) {
        if (v1 < v2)
            return v1;
        else return v2;
    }

    public static long max(long v1, long v2) {
        if (v1 > v2)
            return v1;
        else return v2;
    }

    /**
     * Return true if the two objects are equal, according to their isEquals method.  This method allows the objects to
     * be null, returning true if both are null & false if just one is null.
     *
     * @param o1 object 1
     * @param o2 object 2
     * @return true if both objects are equal or both are null, false otherwise
     */
    public static boolean equal(@Nullable Object o1, @Nullable Object o2) {
        if (o1 == o2)
            return true;
        if (o1 == null || o2 == null)
            return false;
        return o1.equals(o2);
    }

}
