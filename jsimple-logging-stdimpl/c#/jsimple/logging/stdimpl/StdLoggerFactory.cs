using System.Collections.Generic;

namespace jsimple.logging.stdimpl
{

	using StdIO = jsimple.io.StdIO;
	using ILoggerFactory = jsimple.logging.ILoggerFactory;
	using Level = jsimple.logging.Level;
	using Logger = jsimple.logging.Logger;


	/// <summary>
	/// @author Bret Johnson
	/// @since 4/8/13 12:57 AM
	/// </summary>
	public class StdLoggerFactory : ILoggerFactory
	{
		private Dictionary<string, StdLogger> loggers = new Dictionary<string, StdLogger>();
		private Level defaultLevel = Level.DEBUG;
		private IList<Appender> defaultAppenders = new List<Appender>();

		public StdLoggerFactory()
		{
			defaultAppenders.Add(new WriterAppender(StdIO.stdout));
		}

		/// <summary>
		/// Return an appropriate <seealso cref="jsimple.logging.Logger"/> instance as specified by the <code>name</code> parameter.
		/// <p/>
		/// If the name parameter is equal to <seealso cref="jsimple.logging.Logger#ROOT_LOGGER_NAME"/>, that is the string value
		/// "ROOT" (case insensitive), then the root logger of the underlying logging system is returned.
		/// <p/>
		/// Null-valued name arguments are considered invalid.
		/// <p/>
		/// Certain extremely simple logging systems, e.g. NOP, may always return the same logger instance regardless of the
		/// requested name.
		/// </summary>
		/// <param name="name"> the name of the Logger to return </param>
		/// <returns> a Logger instance </returns>
		public virtual Logger getLogger(string name)
		{
			lock (this)
			{
				StdLogger logger = loggers.GetValueOrNull(name);
				if (logger == null)
				{
					logger = new StdLogger(this, name);
					loggers[name] = logger;
				}

				return logger;
			}
		}

		public virtual Level DefaultLevel
		{
			get
			{
				return defaultLevel;
			}
			set
			{
				lock (this)
				{
					this.defaultLevel = value;
    
					foreach (StdLogger logger in loggers.Values)
						logger.updateEffectiveLevel();
				}
			}
		}


		/// <summary>
		/// Return the current default list of appenders that are configured.  The returned list should NOT be modified; it
		/// must be treated as immutable.
		/// </summary>
		/// <returns> configured appenders </returns>
		public virtual IList<Appender> DefaultAppenders
		{
			get
			{
				return defaultAppenders;
			}
			set
			{
				lock (this)
				{
					this.defaultAppenders = value;
    
					foreach (StdLogger logger in loggers.Values)
						logger.updateEffectiveAppenders();
				}
			}
		}

	}

}