namespace jsimple.logging.stdimpl
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 4/8/13 12:39 AM
	/// </summary>
	public abstract class Appender
	{
		public abstract void append(LoggingEvent loggingEvent);
	}

}