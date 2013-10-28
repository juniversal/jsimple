using System;

namespace jsimple.util
{
    public class BasicException : Exception
    {
        public BasicException(Exception cause)
            : base(null, cause)
        {
        }

        public BasicException(String message) : base(message)
        {
        }

        public BasicException(String message, Object arg1)
            : this(MessageFormatter.format(message, arg1))
        {
        }

        public BasicException(String message, Object arg1, Object arg2)
            : this(MessageFormatter.format(message, arg1, arg2))
        {
        }

        public BasicException(String message, params Object[] args)
            : this(MessageFormatter.format(message, args))
        {
        }

        public BasicException(MessageFormatter.FormattingTuple formattingTuple)
            : base(formattingTuple.FormattedMessage, formattingTuple.Throwable)
        {
        }
    }

}