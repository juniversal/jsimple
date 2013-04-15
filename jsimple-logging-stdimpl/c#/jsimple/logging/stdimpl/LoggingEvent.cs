using System;

namespace jsimple.logging.stdimpl
{

	using Level = jsimple.logging.Level;
	using PlatformUtils = jsimple.util.PlatformUtils;

	/// <summary>
	/// @author Bret Johnson
	/// @since 4/8/13 12:20 AM
	/// </summary>
	public class LoggingEvent
	{
		private long timestamp; // Event time of occurrence, in millis
		private string threadName;
		private string loggerName;
		private Level level;
		private string format = null;
		private object[] messageArgs = null;
		private string formattedMessage = null;
		private Exception throwable = null;

		public LoggingEvent(string loggerName, Level level, string format, object[] arguments)
		{
			this.timestamp = PlatformUtils.CurrentTimeMillis;
			this.threadName = "THREAD"; // TODO: Make this real thread name
			this.loggerName = loggerName;
			this.level = level;
			this.format = format;
			this.messageArgs = arguments;
		}

		public LoggingEvent(string loggerName, Level level, string message, Exception t)
		{
			this.timestamp = PlatformUtils.CurrentTimeMillis;
			this.threadName = "THREAD"; // TODO: Make this real thread name
			this.loggerName = loggerName;
			this.level = level;
			this.format = message;
			this.messageArgs = null;
			this.throwable = t;
		}

		public virtual long Timestamp
		{
			get
			{
				return timestamp;
			}
		}

		public virtual string ThreadName
		{
			get
			{
				return threadName;
			}
		}

		public virtual string LoggerName
		{
			get
			{
				return loggerName;
			}
		}

		public virtual Level Level
		{
			get
			{
				return level;
			}
		}

		public virtual string Format
		{
			get
			{
				return format;
			}
		}

		public virtual object[] MessageArgs
		{
			get
			{
				return messageArgs;
			}
		}

		public virtual string FormattedMessage
		{
			get
			{
				return formattedMessage;
			}
		}

		public virtual Exception Throwable
		{
			get
			{
				return throwable;
			}
		}
	}

}