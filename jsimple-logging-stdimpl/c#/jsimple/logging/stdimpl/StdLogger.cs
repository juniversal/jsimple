using System;
using System.Collections.Generic;

namespace jsimple.logging.stdimpl
{

	using Level = jsimple.logging.Level;
	using Logger = jsimple.logging.Logger;


	/// <summary>
	/// @author Bret Johnson
	/// @since 4/8/13 12:10 AM
	/// </summary>
	public class StdLogger : Logger
	{
		private StdLoggerFactory factory;
		private string name;
		volatile private Level loggerLevel = null; // By default, don't override level
		volatile private int effectiveLevel;
		volatile private IList<Appender> loggerAppenders = null; // By default, don't override appenders
		volatile private IList<Appender> effectiveAppenders;

		public StdLogger(StdLoggerFactory factory, string name)
		{
			this.factory = factory;
			this.name = name;

			// By default, don't override level or appenders
			effectiveLevel = factory.DefaultLevel.IntValue;
			effectiveAppenders = factory.DefaultAppenders;
		}

		public override string Name
		{
			get
			{
				return name;
			}
		}

		/// <summary>
		/// Override the level for this logger, or remove the override if loggerLevel is null.  When not overridden the
		/// logger will get the default level, set on the factory, otherwise the override sets the level.
		/// </summary>
		/// <param name="loggerLevel"> level of info to log </param>
		public virtual Level Level
		{
			set
			{
				this.loggerLevel = value;
				updateEffectiveLevel();
			}
		}

		public override bool isLevelEnabled(Level level)
		{
			return level.IntValue >= effectiveLevel;
		}

		public override void logWithVarargs(Level level, string format, params object[] arguments)
		{
			if (isLevelEnabled(level))
				log(new LoggingEvent(name, level, format, arguments));
		}

		public override void log(Level level, string msg, Exception t)
		{
			if (isLevelEnabled(level))
				log(new LoggingEvent(name, level, msg, t));
		}

		/// <summary>
		/// Log the specified logging event to the appenders.  At this point, the isLevelEnabled check is assumed to have
		/// already passed.
		/// </summary>
		/// <param name="loggingEvent"> event info to log </param>
		private void log(LoggingEvent loggingEvent)
		{
			// Store the appender list in a local to ensure it doesn't change while using it
			IList<Appender> currentEffectiveAppenders = effectiveAppenders;

			foreach (Appender appender in currentEffectiveAppenders)
				appender.append(loggingEvent);
		}

		internal virtual void updateEffectiveLevel()
		{
			lock (this)
			{
				// Store the level in a local to ensure it doesn't change while using it
				Level currentLevel = loggerLevel;

				if (currentLevel != null)
					effectiveLevel = currentLevel.IntValue;
				else
					effectiveLevel = factory.DefaultLevel.IntValue;
			}
		}

		internal virtual void updateEffectiveAppenders()
		{
			lock (this)
			{
				// Store the appender list in a local to ensure it doesn't change while using it
				IList<Appender> currentLoggerAppenders = loggerAppenders;

				if (currentLoggerAppenders != null)
					effectiveAppenders = currentLoggerAppenders;
				else
					effectiveAppenders = factory.DefaultAppenders;
			}
		}
	}

}