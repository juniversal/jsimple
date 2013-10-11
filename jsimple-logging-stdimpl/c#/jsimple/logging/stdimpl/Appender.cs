namespace jsimple.logging.stdimpl
{

	/// <summary>
	/// Note that Appenders MUST BE THREAD SAFE!
	/// 
	/// @author Bret Johnson
	/// @since 4/8/13 12:39 AM
	/// </summary>
	public abstract class Appender
	{
		public abstract void append(LoggingEvent loggingEvent);
	}

}