namespace jsimple.json
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 7/8/12 1:22 PM
	/// </summary>
	internal sealed class JsonUtil
	{
		/// <summary>
		/// Return true if the particular character should be considered a control character and specified via Unicode escape
		/// sequence in JSON.  The JSON spec is a bit ambiguous (as far as I can tell) on which exact Unicode characters are
		/// "control characters" and must be escaped, so we have to make some reasonable assumption.  We consider control
		/// characters to be the 65 characters in the ranges U+0000..U+001F and U+007F..U+009F, according to
		/// http://unicode.org/glossary/#control_codes.  Note that this includes '\0', which is the end of string for us when
		/// parsing.
		/// </summary>
		/// <param name="c"> character in question </param>
		/// <returns> whether specified character is a control character </returns>
		internal static bool isControlCharacter(char c)
		{
			return c <= '\u001F' || (c >= '\u007F' && c <= '\u009F');
		}
	}

}