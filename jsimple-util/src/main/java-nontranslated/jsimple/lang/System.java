package jsimple.lang;

/**
 * Created by Bret on 12/11/2014.
 */
public class System extends SystemBase {
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
    public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
        java.lang.System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * Causes the virtual machine to stop running and the program to exit. If
     * {@link #runFinalizersOnExit(boolean)} has been previously invoked with a
     * {@code true} argument, then all objects will be properly
     * garbage-collected and finalized first.
     *
     * @param code
     *            the return code.
     * @throws SecurityException
     *             if the running thread has not enough permission to exit the
     *             virtual machine.
     * @see SecurityManager#checkExit
     */
    public static void exit(int code) {
        java.lang.System.exit(code);
    }
}
