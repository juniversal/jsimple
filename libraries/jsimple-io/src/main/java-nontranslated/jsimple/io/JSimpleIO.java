/*
 * Copyright (c) 2012-2015, Microsoft Mobile
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

package jsimple.io;

import jsimple.net.HttpRequest;
import jsimple.net.JavaHttpRequest;
import jsimple.net.JavaTcpSocketListener;
import jsimple.net.SocketListener;

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
    public static /* synchronized */ void init() {
        if (!initialized) {
            HttpRequest.setFactory(new JavaHttpRequest.JavaHttpRequestFactory());
            SocketListener.setFactory(new JavaTcpSocketListener.JavaSocketListenerFactory());
            Paths.setInstance(new JavaPaths());

            StdIO.init(new JSimpleOutputStreamOnJavaStream(System.out), new JSimpleOutputStreamOnJavaStream(System.err),
                    new JSimpleInputStreamOnJavaStream(System.in));

            initialized = true;
        }
    }
}
