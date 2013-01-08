package jsimple.util;

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
        for (int i = 0; i < length; i++) {
            byteArray[i] = (byte) bytes[i];
        }

        return byteArray;
    }
}
