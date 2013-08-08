using System;

namespace jsimple.util
{

	/// <summary>
	/// This class was based on, and modified from, the Apache Harmony java.util.StringTokenizer class.  Unlike the standard
	/// Java class, this class returns BasicException instead of NoSuchElementException when there are no more tokens & the
	/// caller didn't properly check for that case first (via calling hasMoreElements).  Other that that pretty minor
	/// difference, it's the same as the standard Java class.
	/// <p/>
	/// The {@code StringTokenizer} class allows an application to break a string into tokens by performing code point
	/// comparison. The {@code StringTokenizer} methods do not distinguish among identifiers, numbers, and quoted strings,
	/// nor do they recognize and skip comments.
	/// <p/>
	/// The set of delimiters (the codepoints that separate tokens) may be specified either at creation time or on a
	/// per-token basis.
	/// <p/>
	/// An instance of {@code StringTokenizer} behaves in one of three ways, depending on whether it was created with the
	/// {@code returnDelimiters} flag having the value {@code true} or {@code false}: <ul> <li>If returnDelims is {@code
	/// false}, delimiter code points serve to separate tokens. A token is a maximal sequence of consecutive code points that
	/// are not delimiters. <li>If returnDelims is {@code true}, delimiter code points are themselves considered to be
	/// tokens. In this case a token will be received for each delimiter code point. </ul>
	/// <p/>
	/// A token is thus either one delimiter code point, or a maximal sequence of consecutive code points that are not
	/// delimiters.
	/// <p/>
	/// A {@code StringTokenizer} object internally maintains a current position within the string to be tokenized. Some
	/// operations advance this current position past the code point processed.
	/// <p/>
	/// A token is returned by taking a substring of the string that was used to create the {@code StringTokenizer} object.
	/// <p/>
	/// Here's an example of the use of the default delimiter {@code StringTokenizer} : <blockquote>
	/// <p/>
	/// <pre>
	/// StringTokenizer st = new StringTokenizer(&quot;this is a test&quot;);
	/// while (st.hasMoreTokens()) {
	///     println(st.nextToken());
	/// }
	/// </pre>
	/// <p/>
	/// </blockquote>
	/// <p/>
	/// This prints the following output: <blockquote>
	/// <p/>
	/// <pre>
	///     this
	///     is
	///     a
	///     test
	/// </pre>
	/// <p/>
	/// </blockquote>
	/// <p/>
	/// Here's an example of how to use a {@code StringTokenizer} with a user specified delimiter: <blockquote>
	/// <p/>
	/// <pre>
	/// StringTokenizer st = new StringTokenizer(
	///         &quot;this is a test with supplementary characters \ud800\ud800\udc00\udc00&quot;,
	///         &quot; \ud800\udc00&quot;);
	/// while (st.hasMoreTokens()) {
	///     println(st.nextToken());
	/// }
	/// </pre>
	/// <p/>
	/// </blockquote>
	/// <p/>
	/// This prints the following output: <blockquote>
	/// <p/>
	/// <pre>
	///     this
	///     is
	///     a
	///     test
	///     with
	///     supplementary
	///     characters
	///     \ud800
	///     \udc00
	/// </pre>
	/// <p/>
	/// </blockquote>
	/// </summary>
	public class StringTokenizer
	{
		private string @string;
		private string delimiters;
		private bool returnDelimiters;
		private int position;

		/// <summary>
		/// Constructs a new {@code StringTokenizer} for the parameter string using whitespace as the delimiter. The {@code
		/// returnDelimiters} flag is set to {@code false}.
		/// </summary>
		/// <param name="string"> the string to be tokenized. </param>
		public StringTokenizer(string @string) : this(@string, " \t\n\r\f", false)
		{
		}

		/// <summary>
		/// Constructs a new {@code StringTokenizer} for the parameter string using the specified delimiters. The {@code
		/// returnDelimiters} flag is set to {@code false}. If {@code delimiters} is {@code null}, this constructor doesn't
		/// throw an {@code Exception}, but later calls to some methods might throw a {@code NullPointerException}.
		/// </summary>
		/// <param name="string">     the string to be tokenized. </param>
		/// <param name="delimiters"> the delimiters to use. </param>
		public StringTokenizer(string @string, string delimiters) : this(@string, delimiters, false)
		{
		}

		/// <summary>
		/// Constructs a new {@code StringTokenizer} for the parameter string using the specified delimiters, returning the
		/// delimiters as tokens if the parameter {@code returnDelimiters} is {@code true}. If {@code delimiters} is null
		/// this constructor doesn't throw an {@code Exception}, but later calls to some methods might throw a {@code
		/// NullPointerException}.
		/// </summary>
		/// <param name="string">           the string to be tokenized. </param>
		/// <param name="delimiters">       the delimiters to use. </param>
		/// <param name="returnDelimiters"> {@code true} to return each delimiter as a token. </param>
		public StringTokenizer(string @string, string delimiters, bool returnDelimiters)
		{
			if (@string != null)
			{
				this.@string = @string;
				this.delimiters = delimiters;
				this.returnDelimiters = returnDelimiters;
				this.position = 0;
			}
			else
				throw new System.NullReferenceException();
		}

		/// <summary>
		/// Returns the number of unprocessed tokens remaining in the string.
		/// </summary>
		/// <returns> number of tokens that can be retrieved before an {@code Exception} will result from a call to {@code
		///         nextToken()}. </returns>
		public virtual int countTokens()
		{
			int count = 0;
			bool inToken = false;
			for (int i = position, length = @string.Length; i < length; i++)
			{
				if (delimiters.IndexOf(@string[i], 0) >= 0)
				{
					if (returnDelimiters)
						count++;
					if (inToken)
					{
						count++;
						inToken = false;
					}
				}
				else
					inToken = true;
			}
			if (inToken)
				count++;
			return count;
		}

		/// <summary>
		/// Returns {@code true} if unprocessed tokens remain. This method is implemented in order to satisfy the {@code
		/// Enumeration} interface.
		/// </summary>
		/// <returns> {@code true} if unprocessed tokens remain. </returns>
		public virtual bool hasMoreElements()
		{
			return hasMoreTokens();
		}

		/// <summary>
		/// Returns {@code true} if unprocessed tokens remain.
		/// </summary>
		public virtual bool hasMoreTokens()
		{
			if (delimiters == null)
				throw new System.NullReferenceException();

			int length = @string.Length;
			if (position < length)
			{
				if (returnDelimiters)
					return true; // there is at least one character and even if it is a delimiter it is a token

				// otherwise find a character which is not a delimiter
				for (int i = position; i < length; i++)
					if (delimiters.IndexOf(@string[i], 0) == -1)
						return true;
			}
			return false;
		}

		/// <summary>
		/// Returns the next token in the string as an {@code Object}. This method is implemented in order to satisfy the
		/// {@code Enumeration} interface.
		/// </summary>
		/// <returns> next token in the string as an {@code Object} </returns>
		/// <exception cref="BasicException"> if no tokens remain. </exception>
		public virtual object nextElement()
		{
			return nextToken();
		}

		/// <summary>
		/// Returns the next token in the string as a {@code String}.
		/// </summary>
		/// <returns> next token in the string as a {@code String}. </returns>
		/// <exception cref="BasicException"> if no tokens remain. </exception>
		public virtual string nextToken()
		{
			if (delimiters == null)
				throw new System.NullReferenceException();

			int i = position;
			int length = @string.Length;

			if (i < length)
			{
				if (returnDelimiters)
				{
					if (delimiters.IndexOf(@string[position], 0) >= 0)
						return Convert.ToString(@string[position++]);
					for (position++; position < length; position++)
						if (delimiters.IndexOf(@string[position], 0) >= 0)
							return @string.Substring(i, position - i);
					return @string.Substring(i);
				}

				while (i < length && delimiters.IndexOf(@string[i], 0) >= 0)
					i++;
				position = i;
				if (i < length)
				{
					for (position++; position < length; position++)
						if (delimiters.IndexOf(@string[position], 0) >= 0)
							return @string.Substring(i, position - i);
					return @string.Substring(i);
				}
			}

			throw new BasicException("No more tokens");
		}

		/// <summary>
		/// Returns the next token in the string as a {@code String}. The delimiters used are changed to the specified
		/// delimiters.
		/// </summary>
		/// <param name="delims"> the new delimiters to use. </param>
		/// <returns> next token in the string as a {@code String}. </returns>
		/// <exception cref="BasicException"> if no tokens remain. </exception>
		public virtual string nextToken(string delims)
		{
			this.delimiters = delims;
			return nextToken();
		}
	}

}