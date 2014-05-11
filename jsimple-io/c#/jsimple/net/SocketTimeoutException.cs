using System;

namespace jsimple.net {

    using IOException = jsimple.io.IOException;

    /// <summary>
    /// This is a platform independent class used to represent network timeout exceptions, taking the place of
    /// java.net.SocketTimeoutException in JSimple based code.
    /// 
    /// @author Bret Johnson
    /// @since 10/6/12 5:27 PM
    /// </summary>
    public class SocketTimeoutException : IOException {
        public SocketTimeoutException(Exception cause) : this(cause.ToString(), cause) {
            // TODO: Test to see what cause.toString ends up with in C#
        }

        public SocketTimeoutException(string message) : base(message) {
        }

        public SocketTimeoutException(string message, Exception cause) : base(message, cause) {
        }
    }

}