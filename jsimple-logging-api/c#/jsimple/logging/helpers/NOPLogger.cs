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
namespace jsimple.logging.helpers
{


	/// <summary>
	/// A direct NOP (no operation) implementation of <seealso cref="Logger"/>.
	/// 
	/// @author Bret Johnson modified for JSimple
	/// </summary>
	public class NOPLogger : Logger
	{
		/// <summary>
		/// The unique instance of NOPLogger.
		/// </summary>
		public static readonly NOPLogger NOP_LOGGER = new NOPLogger();

		/// <summary>
		/// There is no point in creating multiple instances of NOPLOgger, except by derived classes, hence the protected
		/// access for the constructor.
		/// </summary>
		protected internal NOPLogger()
		{
		}

		/// <summary>
		/// Always returns the string value "NOP".
		/// </summary>
		public override string Name
		{
			get
			{
				return "NOP";
			}
		}

		/// <summary>
		/// For this logger, don't log anything.
		/// </summary>
		/// <param name="level"> log level </param>
		/// <returns> whether logging should be enabled; the NOPLogger always returns false here </returns>
		public override bool isLevelEnabled(Level level)
		{
			return false;
		}

		public override void logWithVarargs(Level level, string format, params object[] arguments)
		{
		}

		public override void log(Level level, string msg, Exception t)
		{
		}
	}

}