package jsimple.io;

import jsimple.net.HttpRequest;
import jsimple.net.JavaHttpRequest;

/**
 * @author Bret Johnson
 * @since 4/18/13 10:53 PM
 */
public class JSimpleIO {
    private static boolean initialized = false;

    /**
     * This method should be called before jsimple.net is used.  It's normally called at app startup. It initializes any
     * factory classes to use the default implementation appropriate for the current platform.
     */
    public static synchronized void init() {
        if (!initialized) {
            HttpRequest.setFactory(new JavaHttpRequest.JavaHttpRequestFactory());
            Paths.setInstance(new JavaPaths());
            initialized = true;
        }
    }
}
