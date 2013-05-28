using System;

/// <summary>
/// Copyright (c) 2004-2011 QOS.ch
/// All rights reserved.
/// 
/// Permission is hereby granted, free  of charge, to any person obtaining
/// a  copy  of this  software  and  associated  documentation files  (the
/// "Software"), to  deal in  the Software without  restriction, including
/// without limitation  the rights to  use, copy, modify,  merge, publish,
/// distribute,  sublicense, and/or sell  copies of  the Software,  and to
/// permit persons to whom the Software  is furnished to do so, subject to
/// the following conditions:
/// 
/// The  above  copyright  notice  and  this permission  notice  shall  be
/// included in all copies or substantial portions of the Software.
/// 
/// THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
/// EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
/// MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
/// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
/// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
/// OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
/// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
/// 
/// </summary>

namespace jsimple.logging
{

	/// <summary>
	/// The jsimple.logging.Logger interface is the main user entry point of JSimple logging API. It is expected that logging
	/// takes place through concrete implementations of this interface.
	/// <p/>
	/// <h3>Typical usage pattern:</h3>
	/// <pre>
	/// import jsimple.logging.Logger;
	/// import jsimple.logging.LoggerFactory;
	/// 
	/// public class Wombat {
	/// 
	///   <span style="color:green">final static Logger logger = LoggerFactory.getLogger("Wombat");</span>
	///   Integer t;
	///   Integer oldT;
	/// 
	///   public void setTemperature(Integer temperature) {
	///     oldT = t;
	///     t = temperature;
	///     <span style="color:green">logger.debug("Temperature set to {}. Old temperature was {}.", t, oldT);</span>
	///     if(temperature.intValue() > 50) {
	///       <span style="color:green">logger.info("Temperature has risen above 50 degrees.");</span>
	///     }
	///   }
	/// }
	/// </pre>
	/// <p/>
	/// This class was adapted from SLF4J, version 1.7.5.  The main change from SLF4J was that Marker support was removed, as
	/// it's not (yet) supported in JSimple logging.  Also, generic log methods were added, taking a Level parameter, like in
	/// JUL (java util logging) & these methods are used for default implementations.
	/// 
	/// @author Ceki G&uuml;lc&uuml;
	/// @author Bret Johnson modified for JSimple
	/// </summary>
	public abstract class Logger
	{
		/// <summary>
		/// Case insensitive String constant used to retrieve the name of the root logger.
		/// 
		/// @since 1.3
		/// </summary>
		public readonly string ROOT_LOGGER_NAME = "ROOT";

		/// <summary>
		/// Return the name of this <code>Logger</code> instance.
		/// </summary>
		/// <returns> name of this logger instance </returns>
		public abstract string Name {get;}

		/// <summary>
		/// Is the logger instance enabled for the TRACE level?
		/// </summary>
		/// <returns> true if this Logger is enabled for the TRACE level, false otherwise.
		/// @since 1.4 </returns>
		public virtual bool TraceEnabled
		{
			get
			{
				return isLevelEnabled(Level.TRACE);
			}
		}

		/// <summary>
		/// Log a message at the TRACE level.
		/// </summary>
		/// <param name="msg"> the message string to be logged
		/// @since 1.4 </param>
		public virtual void trace(string msg)
		{
			log(Level.TRACE, msg);
		}

		/// <summary>
		/// Log a message at the TRACE level according to the specified format and argument.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the TRACE level.
		/// </summary>
		/// <param name="format"> the format string </param>
		/// <param name="arg">    the argument
		/// @since 1.4 </param>
		public virtual void trace(string format, object arg)
		{
			log(Level.TRACE, format, arg);
		}

		/// <summary>
		/// Log a message at the TRACE level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the TRACE level.
		/// </summary>
		/// <param name="format"> the format string </param>
		/// <param name="arg1">   the first argument </param>
		/// <param name="arg2">   the second argument
		/// @since 1.4 </param>
		public virtual void trace(string format, object arg1, object arg2)
		{
			log(Level.TRACE, format, arg1, arg2);
		}

		/// <summary>
		/// Log a message at the TRACE level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous string concatenation when the logger is disabled for the TRACE level. However, this
		/// variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
		/// method, even if this logger is disabled for TRACE. The variants taking <seealso cref="#trace(String, Object) one"/> and
		/// <seealso cref="#trace(String, Object, Object) two"/> arguments exist solely in order to avoid this hidden cost.
		/// </summary>
		/// <param name="format">    the format string </param>
		/// <param name="arguments"> a list of 3 or more arguments
		/// @since 1.4 </param>
		public virtual void trace(string format, params object[] arguments)
		{
			log(Level.TRACE, format, arguments);
		}

		/// <summary>
		/// Log an exception (throwable) at the TRACE level with an accompanying message.
		/// </summary>
		/// <param name="msg"> the message accompanying the exception </param>
		/// <param name="t">   the exception (throwable) to log
		/// @since 1.4 </param>
		public virtual void trace(string msg, Exception t)
		{
			log(Level.TRACE, msg, t);
		}

		/// <summary>
		/// Is the logger instance enabled for the DEBUG level?
		/// </summary>
		/// <returns> true if this Logger is enabled for the DEBUG level, false otherwise. </returns>
		public virtual bool DebugEnabled
		{
			get
			{
				return isLevelEnabled(Level.DEBUG);
			}
		}

		/// <summary>
		/// Log a message at the DEBUG level.
		/// </summary>
		/// <param name="msg"> the message string to be logged </param>
		public virtual void debug(string msg)
		{
			log(Level.DEBUG, msg);
		}

		/// <summary>
		/// Log a message at the DEBUG level according to the specified format and argument.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the DEBUG level.
		/// </summary>
		/// <param name="format"> the format string </param>
		/// <param name="arg">    the argument </param>
		public virtual void debug(string format, object arg)
		{
			log(Level.DEBUG, format, arg);
		}

		/// <summary>
		/// Log a message at the DEBUG level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the DEBUG level.
		/// </summary>
		/// <param name="format"> the format string </param>
		/// <param name="arg1">   the first argument </param>
		/// <param name="arg2">   the second argument </param>
		public virtual void debug(string format, object arg1, object arg2)
		{
			log(Level.DEBUG, format, arg1, arg2);
		}

		/// <summary>
		/// Log a message at the DEBUG level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous string concatenation when the logger is disabled for the DEBUG level. However, this
		/// variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
		/// method, even if this logger is disabled for DEBUG. The variants taking <seealso cref="#debug(String, Object) one"/> and
		/// <seealso cref="#debug(String, Object, Object) two"/> arguments exist solely in order to avoid this hidden cost.
		/// </summary>
		/// <param name="format">    the format string </param>
		/// <param name="arguments"> a list of 3 or more arguments </param>
		public virtual void debug(string format, params object[] arguments)
		{
			log(Level.DEBUG, format, arguments);
		}

		/// <summary>
		/// Log an exception (throwable) at the DEBUG level with an accompanying message.
		/// </summary>
		/// <param name="msg"> the message accompanying the exception </param>
		/// <param name="t">   the exception (throwable) to log </param>
		public virtual void debug(string msg, Exception t)
		{
			log(Level.DEBUG, msg, t);
		}

		/// <summary>
		/// Is the logger instance enabled for the INFO level?
		/// </summary>
		/// <returns> true if this Logger is enabled for the INFO level, false otherwise. </returns>
		public virtual bool InfoEnabled
		{
			get
			{
				return isLevelEnabled(Level.INFO);
			}
		}

		/// <summary>
		/// Log a message at the INFO level.
		/// </summary>
		/// <param name="msg"> the message string to be logged </param>
		public virtual void info(string msg)
		{
			log(Level.INFO, msg);
		}

		/// <summary>
		/// Log a message at the INFO level according to the specified format and argument.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the INFO level.
		/// </summary>
		/// <param name="format"> the format string </param>
		/// <param name="arg">    the argument </param>
		public virtual void info(string format, object arg)
		{
			log(Level.INFO, format, arg);
		}

		/// <summary>
		/// Log a message at the INFO level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the INFO level.
		/// </summary>
		/// <param name="format"> the format string </param>
		/// <param name="arg1">   the first argument </param>
		/// <param name="arg2">   the second argument </param>
		public virtual void info(string format, object arg1, object arg2)
		{
			log(Level.INFO, format, arg1, arg2);
		}

		/// <summary>
		/// Log a message at the INFO level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous string concatenation when the logger is disabled for the INFO level. However, this
		/// variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
		/// method, even if this logger is disabled for INFO. The variants taking <seealso cref="#info(String, Object) one"/> and
		/// <seealso cref="#info(String, Object, Object) two"/> arguments exist solely in order to avoid this hidden cost.
		/// </summary>
		/// <param name="format">    the format string </param>
		/// <param name="arguments"> a list of 3 or more arguments </param>
		public virtual void info(string format, params object[] arguments)
		{
			log(Level.INFO, format, arguments);
		}

		/// <summary>
		/// Log an exception (throwable) at the INFO level with an accompanying message.
		/// </summary>
		/// <param name="msg"> the message accompanying the exception </param>
		/// <param name="t">   the exception (throwable) to log </param>
		public virtual void info(string msg, Exception t)
		{
			log(Level.INFO, msg, t);
		}

		/// <summary>
		/// Is the logger instance enabled for the WARN level?
		/// </summary>
		/// <returns> true if this Logger is enabled for the WARN level, false otherwise. </returns>
		public virtual bool WarnEnabled
		{
			get
			{
				return isLevelEnabled(Level.WARN);
			}
		}

		/// <summary>
		/// Log a message at the WARN level.
		/// </summary>
		/// <param name="msg"> the message string to be logged </param>
		public virtual void warn(string msg)
		{
			log(Level.WARN, msg);
		}

		/// <summary>
		/// Log a message at the WARN level according to the specified format and argument.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the WARN level.
		/// </summary>
		/// <param name="format"> the format string </param>
		/// <param name="arg">    the argument </param>
		public virtual void warn(string format, object arg)
		{
			log(Level.WARN, format, arg);
		}

		/// <summary>
		/// Log a message at the WARN level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous string concatenation when the logger is disabled for the WARN level. However, this
		/// variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
		/// method, even if this logger is disabled for WARN. The variants taking <seealso cref="#warn(String, Object) one"/> and
		/// <seealso cref="#warn(String, Object, Object) two"/> arguments exist solely in order to avoid this hidden cost.
		/// </summary>
		/// <param name="format">    the format string </param>
		/// <param name="arguments"> a list of 3 or more arguments </param>
		public virtual void warn(string format, params object[] arguments)
		{
			log(Level.WARN, format, arguments);
		}

		/// <summary>
		/// Log a message at the WARN level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the WARN level.
		/// </summary>
		/// <param name="format"> the format string </param>
		/// <param name="arg1">   the first argument </param>
		/// <param name="arg2">   the second argument </param>
		public virtual void warn(string format, object arg1, object arg2)
		{
			log(Level.WARN, format, arg1, arg2);
		}

		/// <summary>
		/// Log an exception (throwable) at the WARN level with an accompanying message.
		/// </summary>
		/// <param name="msg"> the message accompanying the exception </param>
		/// <param name="t">   the exception (throwable) to log </param>
		public virtual void warn(string msg, Exception t)
		{
			log(Level.WARN, msg, t);
		}

		/// <summary>
		/// Is the logger instance enabled for the ERROR level?
		/// </summary>
		/// <returns> true if this Logger is enabled for the ERROR level, false otherwise. </returns>
		public virtual bool ErrorEnabled
		{
			get
			{
				return isLevelEnabled(Level.ERROR);
			}
		}

		/// <summary>
		/// Log a message at the ERROR level.
		/// </summary>
		/// <param name="msg"> the message string to be logged </param>
		public virtual void error(string msg)
		{
			log(Level.ERROR, msg);
		}

		/// <summary>
		/// Log a message at the ERROR level according to the specified format and argument.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the ERROR level.
		/// </summary>
		/// <param name="format"> the format string </param>
		/// <param name="arg">    the argument </param>
		public virtual void error(string format, object arg)
		{
			log(Level.ERROR, format, arg);
		}

		/// <summary>
		/// Log a message at the ERROR level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the ERROR level.
		/// </summary>
		/// <param name="format"> the format string </param>
		/// <param name="arg1">   the first argument </param>
		/// <param name="arg2">   the second argument </param>
		public virtual void error(string format, object arg1, object arg2)
		{
			log(Level.ERROR, format, arg1, arg2);
		}

		/// <summary>
		/// Log a message at the ERROR level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous string concatenation when the logger is disabled for the ERROR level. However, this
		/// variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
		/// method, even if this logger is disabled for ERROR. The variants taking <seealso cref="#error(String, Object) one"/> and
		/// <seealso cref="#error(String, Object, Object) two"/> arguments exist solely in order to avoid this hidden cost.
		/// </summary>
		/// <param name="format">    the format string </param>
		/// <param name="arguments"> a list of 3 or more arguments </param>
		public virtual void error(string format, params object[] arguments)
		{
			log(Level.ERROR, format, arguments);
		}

		/// <summary>
		/// Log an exception (throwable) at the ERROR level with an accompanying message.
		/// </summary>
		/// <param name="msg"> the message accompanying the exception </param>
		/// <param name="t">   the exception (throwable) to log </param>
		public virtual void error(string msg, Exception t)
		{
			log(Level.ERROR, msg, t);
		}

		/// <summary>
		/// Is the logger instance enabled for the specified level?
		/// </summary>
		/// <returns> true if this Logger is enabled for the specified level, false otherwise. </returns>
		public abstract bool isLevelEnabled(Level level);

		/// <summary>
		/// Log a message at the specified level.
		/// </summary>
		/// <param name="level"> logging level to log at </param>
		/// <param name="msg">   the message string to be logged </param>
		public virtual void log(Level level, string msg)
		{
			if (isLevelEnabled(level))
				logWithVarargs(level, msg);
		}

		/// <summary>
		/// Log a message at the specified level according to the specified format and argument.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the level.
		/// </summary>
		/// <param name="level">  logging level to log at </param>
		/// <param name="format"> the format string </param>
		/// <param name="arg">    the argument </param>
		public virtual void log(Level level, string format, object arg)
		{
			if (isLevelEnabled(level))
				logWithVarargs(level, format, arg);
		}

		/// <summary>
		/// Log a message at the specified level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous object creation when the logger is disabled for the level.
		/// </summary>
		/// <param name="level">  logging level to log at </param>
		/// <param name="format"> the format string </param>
		/// <param name="arg1">   the first argument </param>
		/// <param name="arg2">   the second argument </param>
		public virtual void log(Level level, string format, object arg1, object arg2)
		{
			if (isLevelEnabled(level))
				logWithVarargs(level, format, arg1, arg2);
		}

		/// <summary>
		/// Log a message at the specified level according to the specified format and arguments.
		/// <p/>
		/// This form avoids superfluous string concatenation when the logger is disabled for the level. However, this
		/// variant incurs the hidden (and relatively small) cost of creating an <code>Object[]</code> before invoking the
		/// method, even if this logger is disabled for ERROR. The variants taking <seealso cref="#error(String, Object) one"/> and
		/// <seealso cref="#error(String, Object, Object) two"/> arguments exist solely in order to avoid this hidden cost.
		/// </summary>
		/// <param name="level">     logging level to log at </param>
		/// <param name="format">    the format string </param>
		/// <param name="arguments"> a list of 3 or more arguments </param>
		public virtual void log(Level level, string format, params object[] arguments)
		{
			if (isLevelEnabled(level))
				logWithVarargs(level, format, arguments);
		}

		/// <summary>
		/// Log a message at the specified level, with the specified message format and arguments.  This method is the same
		/// as the log varargs method, except that it has a different name, with no overloads, to force use of the varargs
		/// version no matter how few arguments are passed.
		/// </summary>
		/// <param name="level">     logging level to log at </param>
		/// <param name="format">    the format string </param>
		/// <param name="arguments"> a list of 0 or more arguments </param>
		public abstract void logWithVarargs(Level level, string format, params object[] arguments);

		/// <summary>
		/// Log an exception (throwable) at the specified level with an accompanying message.
		/// </summary>
		/// <param name="level"> logging level to log at </param>
		/// <param name="msg">   the message accompanying the exception </param>
		/// <param name="t">     the exception (throwable) to log </param>
		public abstract void log(Level level, string msg, Exception t);

		public virtual LogEnterLeave debugEnterLeave(string msg)
		{
			return logEnterLeave(Level.DEBUG, msg);
		}

		public virtual LogEnterLeave debugEnterLeave(string format, params object[] arguments)
		{
			return logEnterLeave(Level.DEBUG, format, arguments);
		}

		public virtual LogEnterLeave traceEnterLeave(string msg)
		{
			return logEnterLeave(Level.TRACE, msg);
		}

		public virtual LogEnterLeave traceEnterLeave(string format, params object[] arguments)
		{
			return logEnterLeave(Level.TRACE, format, arguments);
		}

		public virtual LogEnterLeave logEnterLeave(Level level, string format, params object[] arguments)
		{
			if (isLevelEnabled(level))
				return logStartAndEndWithVarargs(level, format, arguments);
			else
				return null;
		}

		/// <summary>
		/// Log start and end messages at the specified level, with the specified message format and arguments.  This method
		/// is the same as the logEnterLeave varargs method, except that it has a different name, with no overloads, to
		/// force use of the varargs version no matter how few arguments are passed.
		/// </summary>
		/// <param name="level">     logging level to log at </param>
		/// <param name="format">    the format string </param>
		/// <param name="arguments"> a list of 0 or more arguments </param>
		public abstract LogEnterLeave logStartAndEndWithVarargs(Level level, string format, params object[] arguments);
	}

}