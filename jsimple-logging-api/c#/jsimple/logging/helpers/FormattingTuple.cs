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
	/// Holds the results of formatting done by <seealso cref="MessageFormatter"/>.
	/// 
	/// @author Joern Huxhorn
	/// @author Bret Johnson adapted for JSimple
	/// </summary>
	public class FormattingTuple
	{
		public static FormattingTuple NULL = new FormattingTuple(null);

		private string message;
		private Exception throwable;
		private object[] argArray;

		public FormattingTuple(string message) : this(message, null, null)
		{
		}

		public FormattingTuple(string message, object[] argArray, Exception throwable)
		{
			this.message = message;
			this.throwable = throwable;

			if (throwable == null)
				this.argArray = argArray;
			else
				this.argArray = trimmedCopy(argArray);
		}

		internal static object[] trimmedCopy(object[] argArray)
		{
			if (argArray == null || argArray.Length == 0)
				throw new Exception("non-sensical empty or null argument array");

//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final int trimemdLen = argArray.length - 1;
			int trimemdLen = argArray.Length - 1;
			object[] trimmed = new object[trimemdLen];
			Array.Copy(argArray, 0, trimmed, 0, trimemdLen);
			return trimmed;
		}

		public virtual string Message
		{
			get
			{
				return message;
			}
		}

		public virtual object[] ArgArray
		{
			get
			{
				return argArray;
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