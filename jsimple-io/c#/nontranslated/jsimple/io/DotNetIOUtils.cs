namespace jsimple.io
{
    public class DotNetIOUtils
    {
        /// <summary>
        ///     Map a .Net IOException to the matching JSimple platform independent exception.  If there's a specific exception
        ///     type for the subclass (because it's an error we're likely to care about), return that.  Otherwise, just return a
        ///     JSimple IOException.
        /// </summary>
        public static IOException jSimpleExceptionFromDotNetIOException(System.IO.IOException e)
        {
            IOException jSimpleIOException;
            string message = e.Message ?? "";

            // TODO: Map the other exceptions
            /*
            if (e is java.net.SocketTimeoutException)
                jSimpleIOException = new SocketTimeoutException(message);
            else if (e is UnknownHostException)
                jSimpleIOException = new UnknownHostException(message);
            */
            if (e is System.IO.FileNotFoundException)
                jSimpleIOException = new FileNotFoundException(message);
            else jSimpleIOException = new IOException(message);

            /*
            // Replace the stack trace with the original one, so it looks like the original code threw the exception.
            // In some ways that's not technically correct, but it generally makes the stack traces easier to read
            // and more standard Java-like, since the nested exception wrapping doesn't add any real info in error output
            // and in practice tends to make error output a bit harder to understand, not easier
            StackTraceElement[] stackTrace = e.getStackTrace();
            jSimpleIOException.setStackTrace(stackTrace);
             */

            return jSimpleIOException;
        }
    }
}