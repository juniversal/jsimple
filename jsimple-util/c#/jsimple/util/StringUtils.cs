using System;
using System.Text;

namespace jsimple.util
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 10/23/12 12:23 AM
	/// </summary>
	public class StringUtils
	{

		/// <summary>
		/// Convert a byte array to a hexadecimal string.  Leading zeros are of course preserved.  Hex letters are upper
		/// case.
		/// </summary>
		/// <param name="bytes"> byte array </param>
		/// <returns> hexadecimal string </returns>
		public static string toHexStringFromBytes(sbyte[] bytes)
		{
			char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
			char[] hexChars = new char[bytes.Length * 2];
			int v;
			for (int j = 0; j < bytes.Length; j++)
			{
				v = bytes[j] & 0xFF;
				hexChars[j * 2] = hexArray[(int)((uint)v >> 4)]; // Formerly v/16
				hexChars[j * 2 + 1] = hexArray[v & 0xF]; // Formerly v%16
			}
			return new string(hexChars);
		}

		/// <summary>
		/// Convert a hexadecimal string to a byte array.  An exception is thrown if the string contains any non-hex
		/// characters.  A-F and a-f (both upper & lower case) are allowed for input.
		/// </summary>
		/// <param name="hexString"> hex string, containing only 0-9, A-F, or a-f </param>
		/// <returns> byte array </returns>
		public static sbyte[] toBytesFromHexString(string hexString)
		{
			int len = hexString.Length;
			sbyte[] bytes = new sbyte[len / 2];
			for (int i = 0; i < len; i += 2)
				bytes[i / 2] = (sbyte)((toByteFromHexCharacter(hexString[i]) << 4) + toByteFromHexCharacter(hexString[i + 1]));

			return bytes;
		}

		/// <summary>
		/// Convert a hex character to it's equivalent byte.  Similar to standard Java's Character.digit.  If character isn't
		/// a valid hex character, an exception is thrown.
		/// </summary>
		/// <param name="hexCharacter"> hex character (0-9, A-F, or a-f) </param>
		/// <returns> number equivalent (0-15) </returns>
		public static sbyte toByteFromHexCharacter(char hexCharacter)
		{
			if ('0' <= hexCharacter && hexCharacter <= '9')
				return (sbyte)(hexCharacter - '0');
			else if ('A' <= hexCharacter && hexCharacter <= 'F')
				return (sbyte)(hexCharacter - 'A' + 10);
			else if ('a' <= hexCharacter && hexCharacter <= 'f')
				return (sbyte)(hexCharacter - 'a' + 10);
			else
				throw new Exception("Invalid hex character: '" + hexCharacter + "'");
		}

		/// <summary>
		/// Convert a byte array, assumed to be a Latin1 (aka 8859-1 aka Windows 1252) encoded string, to a regular Java
		/// string.  Latin1 is the first 256 characters of Unicode, so this conversion is just a straight pass through,
		/// turning bytes in chars.  Note that ASCII is a subset of Latin1.
		/// </summary>
		/// <param name="bytes"> byte array </param>
		/// <returns> resulting string </returns>
		public static string toStringFromLatin1Bytes(sbyte[] bytes)
		{
			int length = bytes.Length;
			char[] chars = new char[length];
			for (int i = 0; i < length; i++)
				chars[i] = (char) bytes[i];
			return new string(chars);
		}

		/// <summary>
		/// Convert a string, assumed to contain only Latin1 (aka 8859-1 aka Windows 1252) characters, to a byte array.  The
		/// byte array is encoded one byte per character--just a straight pass through from the Unicode, dropping the high
		/// byte (which should be zero) of each character.  If any non-Latin1 characters are in the string an exception is
		/// thrown.  This method is an efficient way to only ASCII text as a byte array, since ASCII is a subset of Latin1.
		/// </summary>
		/// <param name="string"> input string, which should contain only Latin1 characters </param>
		/// <returns> resulting byte array </returns>
		public static sbyte[] toLatin1BytesFromString(string @string)
		{
			int length = @string.Length;
			sbyte[] bytes = new sbyte[length];
			for (int i = 0; i < length; i++)
			{
				char c = @string[i];
				if (c >= 256)
					throw new Exception("Character '" + c + "' at index + " + i + " isn't a Latin1 character");
				bytes[i] = (sbyte) c;
			}
			return bytes;
		}

		/// <summary>
		/// Append the specified UTF-32 code point to the string builder, encoding it in UTF-16 (as is used for standard Java
		/// and C# strings) as a single 16 bit character if it's in the basic multilingual plane or as a surrogate pair if
		/// it's in one of the supplementary planes.  See http://en.wikipedia.org/wiki/UTF-16 for details.
		/// </summary>
		/// <param name="s"> </param>
		/// <param name="utf32Character"> </param>
		public static void appendCodePoint(StringBuilder s, int utf32Character)
		{
			if ((utf32Character >= 0x0000 && utf32Character <= 0xd7ff) || (utf32Character >= 0xe000 && utf32Character <= 0xffff))
				s.Append((char) utf32Character);
			else if (utf32Character >= 0x10000 && utf32Character <= 0x10ffff)
			{
				int value = utf32Character - 0x10000; // 20 bit value in the range 0..0xFFFFF
				int leadSurrogate = (value >> 0x3FF) + 0xD800; // 10 bits
				int trailSurrogate = (value & 0x3FF) + 0xDC00; // 10 bits
				s.Append((char) leadSurrogate);
				s.Append((char) trailSurrogate);
			}
			else
				throw new Exception("Invalid UTF-32 character code:" + utf32Character);
		}
	}

}