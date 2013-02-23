using System;
using System.Text;

namespace jsimple.util
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 11/25/12 4:00 PM
	/// </summary>
	public class CharIterator
	{
		private string str;
		private int index = 0;
		private int length;

		public CharIterator(string @string)
		{
			this.str = @string;
			this.length = @string.Length;
		}

		public virtual bool atEnd()
		{
			return index >= length;
		}

		/// <returns> current character or '\0' if at end of str </returns>
		public virtual char curr()
		{
			if (index >= length)
				return '\0';
			else
				return str[index];
		}

		/// <summary>
		/// Return the current character (same as curr()) then advance to the next one.
		/// </summary>
		/// <returns> current character or -1 if at end of str </returns>
		public virtual char currAndAdvance()
		{
			char c = curr();
			advance();
			return c;
		}

		/// <summary>
		/// Advance the iterator to the next character.  If already at the end of the str, this method is a no-op.
		/// </summary>
		public virtual void advance()
		{
			if (index < length)
				++index;
		}

		/// <summary>
		/// Search for the the next occurrence of the specified substring & advance the iterator just past it.  If that
		/// substring isn't found, an exception is thrown.
		/// </summary>
		/// <param name="substr"> substring to search for </param>
		public virtual void skipAheadPast(string substr)
		{
			if (!skipAheadPastIfExists(substr))
				throw new Exception("'" + substr + "' not found in string '" + str + "'");
		}

		/// <summary>
		/// Search for the the next occurrence of the specified substring & advance the iterator just past it.  Return true
		/// if the substring is found, false if it isn't.
		/// </summary>
		/// <param name="substr"> substring to search for </param>
		/// <returns> true if the substring is found </returns>
		public virtual bool skipAheadPastIfExists(string substr)
		{
			int substringIndex = str.IndexOf(substr, index);
			if (substringIndex == -1)
				return false;

			index = substringIndex + substr.Length;
			return true;
		}

		/// <summary>
		/// Verify that the current character is c, throwing an exception if it isn't, then advance to the next character.
		/// </summary>
		/// <param name="c"> character to verify that occurs at the current iterator position </param>
		public virtual void checkAndAdvancePast(char c)
		{
			if (atEnd())
				throw new Exception("Already at end of string");

			if (str[index] != c)
				throw new Exception("Character '" + c + "' not found at this position in the string: " + str.Substring(index));

			++index;
		}

		/// <returns> true if the current character is a whitespace character. </returns>
		public virtual bool Whitespace
		{
			get
			{
				int c = curr();
				return c == ' ' || c == '\t' || c == '\r' || c == '\n';
			}
		}

		/// <summary>
		/// Advance past any whitespace characters in the str, leaving the iterator at the first non-whitespace character
		/// following.
		/// </summary>
		public virtual void advancePastWhitespace()
		{
			while (Whitespace)
				advance();
		}

		public virtual string readWhitespaceDelimitedToken()
		{
			StringBuilder token = new StringBuilder();
			while (!Whitespace && !atEnd())
				token.Append(currAndAdvance());
			return token.ToString();
		}
	}

}