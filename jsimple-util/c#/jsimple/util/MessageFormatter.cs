using System;
using System.Text;

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
namespace jsimple.util
{



	/// <summary>
	/// Formats messages according to very simple substitution rules. Substitutions
	/// can be made 1, 2 or more arguments.
	/// <p/>
	/// <p/>
	/// For example,
	/// <p/>
	/// <pre>
	/// MessageFormatter.format("Hi {}.", "there")
	/// </pre>
	/// <p/>
	/// will return the string "Hi there.".
	/// <p/>
	/// The {} pair is called the <em>formatting anchor</em>. It serves to designate
	/// the location where arguments need to be substituted within the message
	/// pattern.
	/// <p/>
	/// In case your message contains the '{' or the '}' character, you do not have
	/// to do anything special unless the '}' character immediately follows '{'. For
	/// example,
	/// <p/>
	/// <pre>
	/// MessageFormatter.format("Set {1,2,3} is not equal to {}.", "1,2");
	/// </pre>
	/// <p/>
	/// will return the string "Set {1,2,3} is not equal to 1,2.".
	/// <p/>
	/// <p/>
	/// If for whatever reason you need to place the string "{}" in the message
	/// without its <em>formatting anchor</em> meaning, then you need to escape the
	/// '{' character with '\', that is the backslash character. Only the '{'
	/// character should be escaped. There is no need to escape the '}' character.
	/// For example,
	/// <p/>
	/// <pre>
	/// MessageFormatter.format("Set \\{} is not equal to {}.", "1,2");
	/// </pre>
	/// <p/>
	/// will return the string "Set {} is not equal to 1,2.".
	/// <p/>
	/// <p/>
	/// The escaping behavior just described can be overridden by escaping the escape
	/// character '\'. Calling
	/// <p/>
	/// <pre>
	/// MessageFormatter.format("File name is C:\\\\{}.", "file.zip");
	/// </pre>
	/// <p/>
	/// will return the string "File name is C:\file.zip".
	/// <p/>
	/// <p/>
	/// The formatting conventions are different than those of <seealso cref="java.text.MessageFormat"/>
	/// which ships with the Java platform. This is justified by the fact that
	/// SLF4J's implementation is 10 times faster than that of <seealso cref="java.text.MessageFormat"/>.
	/// This local performance difference is both measurable and significant in the
	/// larger context of the complete logging processing chain.
	/// <p/>
	/// <p/>
	/// See also <seealso cref="#format(String, Object)"/>,
	/// <seealso cref="#format(String, Object, Object)"/> and
	/// <seealso cref="#arrayFormat(String, Object[])"/> methods for more details.
	/// <p/>
	/// <p/>
	/// This class was adapted for JSimple from SLF4J, version 1.7.5  The primary change was removing support for including
	/// array contents in formatted messages with array args.  That was primarily made to reduce code size and make things
	/// a bit simpler, keeping with the JSimple philosophy.  Callers that wish to have fancy formatting for any logged
	/// messages they have with array arguments can create a wrapper class with a toString method that formats as they
	/// desire.  In many cases that's more useful anyway as it gives the caller control in how the array data should be
	/// formatted, so it's most user readable and not excessively big (e.g. maybe they only want to log first X elements).
	/// 
	/// @author Ceki G&uuml;lc&uuml;
	/// @author Joern Huxhorn
	/// @author Bret Johnson modified for JSimple
	/// </summary>
	public sealed class MessageFormatter
	{
		internal const char DELIM_START = '{';
		internal const string DELIM_STR = "{}";
		private const char ESCAPE_CHAR = '\\';

		/// <summary>
		/// Performs single argument substitution for the 'messagePattern' passed as
		/// parameter.
		/// <p/>
		/// For example,
		/// <p/>
		/// <pre>
		/// MessageFormatter.format("Hi {}.", "there");
		/// </pre>
		/// <p/>
		/// will return the string "Hi there.".
		/// <p/>
		/// </summary>
		/// <param name="messagePattern"> The message pattern which will be parsed and formatted </param>
		/// <param name="arg">            The argument to be substituted in place of the formatting anchor </param>
		/// <returns> The formatted message </returns>
		public static FormattingTuple format(string messagePattern, object arg)
		{
			return arrayFormat(messagePattern, new object[]{arg});
		}

		/// <summary>
		/// Performs a two argument substitution for the 'messagePattern' passed as
		/// parameter.
		/// <p/>
		/// For example,
		/// <p/>
		/// <pre>
		/// MessageFormatter.format("Hi {}. My name is {}.", "Alice", "Bob");
		/// </pre>
		/// <p/>
		/// will return the string "Hi Alice. My name is Bob.".
		/// </summary>
		/// <param name="messagePattern"> The message pattern which will be parsed and formatted </param>
		/// <param name="arg1">           The argument to be substituted in place of the first formatting
		///                       anchor </param>
		/// <param name="arg2">           The argument to be substituted in place of the second formatting
		///                       anchor </param>
		/// <returns> The formatted message </returns>
//JAVA TO C# CONVERTER WARNING: 'final' parameters are not allowed in .NET:
//ORIGINAL LINE: public static FormattingTuple format(final String messagePattern, Object arg1, Object arg2)
		public static FormattingTuple format(string messagePattern, object arg1, object arg2)
		{
			return arrayFormat(messagePattern, new object[]{arg1, arg2});
		}

		internal static Exception getThrowableCandidate(object[] argArray)
		{
			if (argArray.Length == 0)
				return null;

//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final Object lastEntry = argArray[argArray.length - 1];
			object lastEntry = argArray[argArray.Length - 1];
			if (lastEntry is Exception)
				return (Exception) lastEntry;
			return null;
		}

		/// <summary>
		/// Same principle as the <seealso cref="#format(String, Object)"/> and <seealso cref="#format(String, Object, Object)"/> methods except
		/// that any number of arguments can be passed in an array.
		/// </summary>
		/// <param name="messagePattern"> The message pattern which will be parsed and formatted </param>
		/// <param name="argArray">       An array of arguments to be substituted in place of formatting
		///                       anchors </param>
		/// <returns> The formatted message </returns>
//JAVA TO C# CONVERTER WARNING: 'final' parameters are not allowed in .NET:
//ORIGINAL LINE: public static FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray)
		public static FormattingTuple arrayFormat(string messagePattern, object[] argArray)
		{
			Exception throwableCandidate = getThrowableCandidate(argArray);

			StringBuilder buffer = new StringBuilder(messagePattern.Length + 50);

			int i = 0;
			int L;
			for (L = 0; L < argArray.Length; L++)
			{
				int j = messagePattern.IndexOf(DELIM_STR, i);

				if (j == -1)
				{
					// no more variables
					if (i == 0) // this is a simple string
						return new FormattingTuple(messagePattern, argArray, throwableCandidate);
					else
					{
						// add the tail string which contains no variables and return the result.
						buffer.Append(messagePattern.Substring(i, messagePattern.Length - i));
						return new FormattingTuple(buffer.ToString(), argArray, throwableCandidate);
					}
				}
				else
				{
					if (isEscapedDelimiter(messagePattern, j))
					{
						if (!isDoubleEscaped(messagePattern, j))
						{
							L--; // DELIM_START was escaped, thus should not be incremented
							buffer.Append(messagePattern.Substring(i, j - 1 - i));
							buffer.Append(DELIM_START);
							i = j + 1;
						}
						else
						{
							// The escape character preceding the delimiter start is itself escaped: "abc x:\\{}"
							// We have to consume one backward slash
							buffer.Append(messagePattern.Substring(i, j - 1 - i));
							deeplyAppendParameter(buffer, argArray[L]);
							i = j + 2;
						}
					}
					else
					{
						// normal case
						buffer.Append(messagePattern.Substring(i, j - i));
						deeplyAppendParameter(buffer, argArray[L]);
						i = j + 2;
					}
				}
			}

			// Append the characters following the last {} pair.
			buffer.Append(messagePattern.Substring(i, messagePattern.Length - i));
			if (L < argArray.Length - 1)
				return new FormattingTuple(buffer.ToString(), argArray, throwableCandidate);
			else
				return new FormattingTuple(buffer.ToString(), argArray, null);
		}

		internal static bool isEscapedDelimiter(string messagePattern, int delimiterStartIndex)
		{
			if (delimiterStartIndex == 0)
				return false;

			char potentialEscape = messagePattern[delimiterStartIndex - 1];
			return potentialEscape == ESCAPE_CHAR;

		}

		internal static bool isDoubleEscaped(string messagePattern, int delimiterStartIndex)
		{
			return delimiterStartIndex >= 2 && messagePattern[delimiterStartIndex - 2] == ESCAPE_CHAR;
		}

		private static void deeplyAppendParameter(StringBuilder buffer, object o)
		{
			if (o == null)
				buffer.Append("null");
			else
			{
				try
				{
					string oAsString = o.ToString();
					buffer.Append(oAsString);
				}
				catch (Exception t)
				{
					buffer.Append("[FAILED toString(); toString threw exception: " + t.ToString() + "]");
				}
			}
		}

		/// <summary>
		/// Holds the results of formatting done by <seealso cref="MessageFormatter"/>.
		/// 
		/// @author Joern Huxhorn
		/// @author Bret Johnson adapted for JSimple
		/// </summary>
		public class FormattingTuple
		{
			public static FormattingTuple NULL = new FormattingTuple(null);

			internal string formattedMessage;
			internal Exception throwable;
			internal object[] argArray;

			public FormattingTuple(string formattedMessage) : this(formattedMessage, null, null)
			{
			}

			public FormattingTuple(string formattedMessage, object[] argArray, Exception throwable)
			{
				this.formattedMessage = formattedMessage;
				this.throwable = throwable;

				if (throwable == null)
					this.argArray = argArray;
				else
					this.argArray = trimmedCopy(argArray);
			}

//JAVA TO C# CONVERTER TODO TASK: Most Java annotations will not have direct .NET equivalent attributes:
//ORIGINAL LINE: static Object[] trimmedCopy(@javax.annotation.Nullable Object[] argArray)
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

			public virtual string FormattedMessage
			{
				get
				{
					return formattedMessage;
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

}