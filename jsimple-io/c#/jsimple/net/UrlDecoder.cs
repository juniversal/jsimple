using System;
using System.Text;

namespace jsimple.net
{

	using IOUtils = jsimple.io.IOUtils;
	using StringTokenizer = jsimple.util.StringTokenizer;
	using StringUtils = jsimple.util.StringUtils;
	using TextualPath = jsimple.util.TextualPath;

	/// <summary>
	/// This class was based on, and modified from, the Apache Harmony java.net.URLDecoder class.  Unlike the standard Java
	/// class, it only supports UTF-8 character encoding (as is the W3C standard for URLs).
	/// 
	/// @author Bret Johnson
	/// @since 11/25/12 8:33 PM
	/// </summary>
	public class UrlDecoder
	{

		/// <summary>
		/// Decodes the argument which is assumed to be encoded in the {@code x-www-form-urlencoded} MIME content type using
		/// the specified encoding scheme.
		/// <p/>
		/// '+' will be converted to space, '%' and two following hex digit characters are converted to the equivalent byte
		/// value. All other characters are passed through unmodified. For example "A+B+C %24%25" -> "A B C $%".
		/// </summary>
		/// <param name="s"> the encoded string </param>
		/// <returns> the decoded clear-text representation of the given string </returns>
		public static string decode(string s)
		{
			if (s.IndexOf('%') == -1)
			{
				if (s.IndexOf('+') == -1)
					return s;

				char[] str = s.ToCharArray();
				for (int i = 0; i < str.Length; i++)
				{
					if (str[i] == '+')
						str[i] = ' ';
				}

				return new string(str);
			}
			else
				return decodeEncodedString(s);
		}

		private static string decodeEncodedString(string s)
		{
			StringBuilder str_buf = new StringBuilder();
			//char str_buf[] = new char[s.length()];
			sbyte[] buf = new sbyte[s.Length / 3];

			for (int i = 0; i < s.Length;)
			{
				char c = s[i];
				if (c == '+')
					str_buf.Append(' ');
				else if (c == '%')
				{
					int len = 0;
					do
					{
						if (i + 2 >= s.Length)
							throw new Exception("Incomplete % sequence at: " + i);

						int d1 = StringUtils.toByteFromHexCharacter(s[i + 1]);
						int d2 = StringUtils.toByteFromHexCharacter(s[i + 2]);

						buf[len++] = (sbyte)((d1 << 4) + d2);
						i += 3;
					} while (i < s.Length && s[i] == '%');

					str_buf.Append(IOUtils.toStringFromUtf8Bytes(buf, 0, len));
					continue;
				}
				else
					str_buf.Append(c);
				i++;
			}

			return str_buf.ToString();
		}

		/// <summary>
		/// Decode a path, delimited with "/" characters.  Note that decoding then looking for / characters isn't completely
		/// correct--that produces the wrong result if elements of the path contact URL encoded / characters not intended to
		/// be path delimiters.  That's one reason for this method's existence--it first finds the / characters then decodes
		/// each element of the path, producing the correct result.
		/// </summary>
		/// <param name="pathString"> path string </param>
		/// <returns> TextualPath, holding a list of strings forming the elements of the path </returns>
		public static TextualPath decodePath(string pathString)
		{
			TextualPath path = new TextualPath();

			StringTokenizer stringTokenizer = new StringTokenizer(pathString, "/");
			while (stringTokenizer.hasMoreTokens())
			{
				string pathElement = decode(stringTokenizer.nextToken());
				path.add(pathElement);
			}

			return path;
		}
	}

}