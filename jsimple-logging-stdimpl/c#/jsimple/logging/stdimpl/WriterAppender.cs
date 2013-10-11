using System;

namespace jsimple.logging.stdimpl
{

	using Writer = jsimple.io.Writer;
	using PlatformUtils = jsimple.util.PlatformUtils;



	/// <summary>
	/// @author Bret Johnson
	/// @since 4/11/13 4:10 PM
	/// </summary>
	public class WriterAppender : Appender
	{
		internal Writer writer;
		internal bool flushImmediately = true;

		public WriterAppender(Writer writer)
		{
			this.writer = writer;
		}

		public override void append(LoggingEvent loggingEvent)
		{
			lock (this)
			{
				writer.writeln(loggingEvent.Level.DefaultDisplayName + " " + loggingEvent.LoggerName + " - " + loggingEvent.FormattedMessage);

				Exception throwable = loggingEvent.Throwable;
				if (throwable != null)
					writer.writeln(PlatformUtils.getExceptionDescription(throwable));

				if (flushImmediately)
					writer.flush();
			}

			/*
			#logback.classic pattern: %d [%thread] %-5level %logger{36} - %msg%n
			2012-04-26 14:54:38,461 [main] DEBUG com.foo.App - Hello world
			2012-04-26 14:54:38,461 [main] DEBUG com.foo.App - Hi again
			*/

			//To change body of implemented methods use File | Settings | File Templates.
		}
	}

}