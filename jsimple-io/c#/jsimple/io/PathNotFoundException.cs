using System;

namespace jsimple.io {

    /// <summary>
    /// This is a platform independent class used to represent file not found exceptions, taking the place of
    /// java.io.FileNotFoundException in JSimple based code.
    /// 
    /// @author Bret Johnson
    /// @since 10/9/12 12:15 PM
    /// </summary>
    public class PathNotFoundException : IOException {
        public PathNotFoundException(Exception cause) : base(cause) {
        }

        public PathNotFoundException(string message) : base(message) {
        }

        public PathNotFoundException(string message, object arg1) : base(message, arg1) {
        }

        public PathNotFoundException(string message, object arg1, object arg2) : base(message, arg1, arg2) {
        }
    }

}