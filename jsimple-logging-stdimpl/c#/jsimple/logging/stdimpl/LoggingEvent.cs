using System;

/// <summary>
/// Parts of this functionality were adapted from Logback (v. 1.0.120:
/// 
/// Logback: the reliable, generic, fast and flexible logging framework.
/// Copyright (C) 1999-2013, QOS.ch. All rights reserved.
/// 
/// This program and the accompanying materials are dual-licensed under
/// either the terms of the Eclipse Public License v1.0 as published by
/// the Eclipse Foundation
/// 
///   or (per the licensee's choosing)
/// 
/// under the terms of the GNU Lesser General Public License version 2.1
/// as published by the Free Software Foundation.
/// </summary>

namespace jsimple.logging.stdimpl
{

	using MessageFormatter = jsimple.util.MessageFormatter;
	using PlatformUtils = jsimple.util.PlatformUtils;

	/// <summary>
	/// @author Bret Johnson
	/// @author Ceki G&uuml;lc&uuml;   (original Logback source)
	/// @author S&eacute;bastien Pennec    (original Logback source)
	/// @since 4/8/13 12:20 AM
	/// </summary>
	public class LoggingEvent
	{
		private long timestamp; // Event time of occurrence, in millis
		private string threadName;
		private string loggerName;
		private Level level;
		private string message = null;
		private object[] messageArgs = null;
		private string formattedMessage = null;
		private Exception throwable = null;

		public LoggingEvent(string loggerName, Level level, string message, object[] argArray)
		{
			this.timestamp = PlatformUtils.CurrentTimeMillis;
			this.loggerName = loggerName;
			this.level = level;

			this.message = message;

			MessageFormatter.FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, argArray);
			formattedMessage = formattingTuple.FormattedMessage;
			messageArgs = formattingTuple.ArgArray;
			this.throwable = formattingTuple.Throwable;

			this.threadName = "THREAD"; // TODO: Make this real thread name
		}

		public LoggingEvent(string loggerName, Level level, string message, Exception t)
		{
			this.timestamp = PlatformUtils.CurrentTimeMillis;
			this.loggerName = loggerName;
			this.level = level;

			this.message = message;
			this.formattedMessage = message;
			this.messageArgs = null;
			this.throwable = t;

			this.threadName = "THREAD"; // TODO: Make this real thread name
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

		public virtual string Message
		{
			get
			{
				return message;
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