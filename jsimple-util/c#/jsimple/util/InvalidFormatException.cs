namespace jsimple.util {

    /// <summary>
    /// This is a generic exception class used to indicate the format of something isn't what it's excepted to be.
    /// 
    /// @author Bret Johnson
    /// @since 5/5/13 4:48 AM
    /// </summary>
    public class InvalidFormatException : BasicException {
        public InvalidFormatException(string message) : base(message) {
        }

        public InvalidFormatException(string message, object arg1) : base(message, arg1) {
        }

        public InvalidFormatException(string message, object arg1, object arg2) : base(message, arg1, arg2) {
        }

        public InvalidFormatException(string message, params object[] args) : base(message, args) {
        }
    }

}