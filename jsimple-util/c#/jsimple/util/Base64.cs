using System;

namespace jsimple.util
{


	/// <summary>
	/// Provides Base64 encoding and decoding as defined by RFC 2045. <p/> <para>This class implements section <cite>6.8. Base64
	/// Content-Transfer-Encoding</cite> from RFC 2045 <cite>Multipurpose Internet Mail Extensions (MIME) Part One: Format of
	/// Internet Message Bodies</cite> by Freed and Borenstein.</para>
	/// <p/>
	/// Sent to us from Nurmi Raimo.T (Nokia-MP/Espoo) <raimo.t.nurmi@nokia.com> & Lindholm Samu (Nokia-MP/Espoo)
	/// <Samu.Lindholm@nokia.com>.  It's original package name was "mango.client.util".
	/// 
	/// @author Apache Software Foundation
	/// @version $Id: Base64.java,v 1.20 2004/05/24 00:21:24 ggregory Exp $ </summary>
	/// <seealso cref= <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>
	/// @since 1.0-dev </seealso>
	public class Base64
	{
		/// <summary>
		/// Chunk size per RFC 2045 section 6.8. <p/> <para>The {@value} character limit does not count the trailing CRLF, but
		/// counts all other characters, including any equal signs.</para>
		/// </summary>
		/// <seealso cref= <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045 section 6.8</a> </seealso>
		internal const int CHUNK_SIZE = 76;

		/// <summary>
		/// Chunk separator per RFC 2045 section 2.1.
		/// </summary>
		/// <seealso cref= <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045 section 2.1</a> </seealso>
		internal static readonly sbyte[] CHUNK_SEPARATOR = new sbyte[] {0xC, 0xA}; // Formerly "\r\n".getBytes();

		/// <summary>
		/// The base length.
		/// </summary>
		internal const int BASELENGTH = 255;

		/// <summary>
		/// Lookup length.
		/// </summary>
		internal const int LOOKUPLENGTH = 64;

		/// <summary>
		/// Used to calculate the number of bits in a byte.
		/// </summary>
		internal const int EIGHTBIT = 8;

		/// <summary>
		/// Used when encoding something which has fewer than 24 bits.
		/// </summary>
		internal const int SIXTEENBIT = 16;

		/// <summary>
		/// Used to determine how many bits data contains.
		/// </summary>
		internal const int TWENTYFOURBITGROUP = 24;

		/// <summary>
		/// Used to get the number of Quadruples.
		/// </summary>
		internal const int FOURBYTE = 4;

		/// <summary>
		/// Used to test the sign of a byte.
		/// </summary>
		internal const int SIGN = -128;

		/// <summary>
		/// Byte used to pad output.
		/// </summary>
		internal static readonly sbyte PAD = (sbyte) '=';

		// Create arrays to hold the base64 characters and a
		// lookup for base64 chars
		private static sbyte[] base64Alphabet = new sbyte[BASELENGTH];
		private static sbyte[] lookUpBase64Alphabet = new sbyte[LOOKUPLENGTH];

		// Populating the lookup and character arrays
		static Base64()
		{
			for (int i = 0; i < BASELENGTH; i++)
				base64Alphabet[i] = (sbyte) -1;
			for (int i = 'Z'; i >= 'A'; i--)
				base64Alphabet[i] = (sbyte)(i - 'A');
			for (int i = 'z'; i >= 'a'; i--)
				base64Alphabet[i] = (sbyte)(i - 'a' + 26);
			for (int i = '9'; i >= '0'; i--)
				base64Alphabet[i] = (sbyte)(i - '0' + 52);

			base64Alphabet['+'] = 62;
			base64Alphabet['/'] = 63;

			for (int i = 0; i <= 25; i++)
				lookUpBase64Alphabet[i] = (sbyte)('A' + i);

			for (int i = 26, j = 0; i <= 51; i++, j++)
				lookUpBase64Alphabet[i] = (sbyte)('a' + j);

			for (int i = 52, j = 0; i <= 61; i++, j++)
				lookUpBase64Alphabet[i] = (sbyte)('0' + j);

			lookUpBase64Alphabet[62] = (sbyte) '+';
			lookUpBase64Alphabet[63] = (sbyte) '/';
		}

		private static bool isBase64(sbyte octect)
		{
			if (octect == PAD)
				return true;
			else if (base64Alphabet[octect] == -1)
				return false;
			else
				return true;
		}

		/// <summary>
		/// Encodes binary data using the base64 algorithm but does not chunk the output.
		/// </summary>
		/// <param name="binaryData"> binary data to encode </param>
		/// <returns> Base64 characters </returns>
		public static sbyte[] encodeBase64(sbyte[] binaryData)
		{
			return encodeBase64(binaryData, false);
		}

		/// <summary>
		/// Encodes binary data using the base64 algorithm but does not chunk the output.  Returns the Base64 data as a
		/// string (which contains at most 64 unique characters).
		/// </summary>
		/// <param name="binaryData"> binary data to encode </param>
		/// <returns> Base64 characters, as a string </returns>
		public static string encodeBase64AsString(sbyte[] binaryData)
		{
			return StringUtils.toStringFromLatin1Bytes(encodeBase64(binaryData, false));
		}

		/// <summary>
		/// Encodes binary data using the base64 algorithm, optionally chunking the output into 76 character blocks.
		/// </summary>
		/// <param name="binaryData"> Array containing binary data to encode. </param>
		/// <param name="isChunked">  if isChunked is true this encoder will chunk the base64 output into 76 character blocks </param>
		/// <returns> Base64-encoded data. </returns>
		public static sbyte[] encodeBase64(sbyte[] binaryData, bool isChunked)
		{
			int lengthDataBits = binaryData.Length * EIGHTBIT;
			int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
			int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
			sbyte[] encodedData = null;
			int encodedDataLength = 0;
			int nbrChunks = 0;

			if (fewerThan24bits != 0)
				//data not divisible by 24 bit
				encodedDataLength = (numberTriplets + 1) * 4;
			else
				// 16 or 8 bit
				encodedDataLength = numberTriplets * 4;

			// If the output is to be "chunked" into 76 character sections,
			// for compliance with RFC 2045 MIME, then it is important to
			// allow for extra length to account for the separator(s)
			if (isChunked)
			{
				nbrChunks = (CHUNK_SEPARATOR.Length == 0 ? 0 : (int) Math.Ceiling((float) encodedDataLength / CHUNK_SIZE));
				encodedDataLength += nbrChunks * CHUNK_SEPARATOR.Length;
			}

			encodedData = new sbyte[encodedDataLength];

			sbyte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

			int encodedIndex = 0;
			int dataIndex = 0;
			int i = 0;
			int nextSeparatorIndex = CHUNK_SIZE;
			int chunksSoFar = 0;

			for (i = 0; i < numberTriplets; i++)
			{
				dataIndex = i * 3;
				b1 = binaryData[dataIndex];
				b2 = binaryData[dataIndex + 1];
				b3 = binaryData[dataIndex + 2];

				//log.debug("b1= " + b1 +", b2= " + b2 + ", b3= " + b3);

				l = (sbyte)(b2 & 0x0f);
				k = (sbyte)(b1 & 0x03);

				sbyte val1 = ((b1 & SIGN) == 0) ? (sbyte)(b1 >> 2) : (sbyte)((b1) >> 2 ^ 0xc0);
				sbyte val2 = ((b2 & SIGN) == 0) ? (sbyte)(b2 >> 4) : (sbyte)((b2) >> 4 ^ 0xf0);
				sbyte val3 = ((b3 & SIGN) == 0) ? (sbyte)(b3 >> 6) : (sbyte)((b3) >> 6 ^ 0xfc);

				encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
				encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | (k << 4)];
				encodedData[encodedIndex + 2] = lookUpBase64Alphabet[(l << 2) | val3];
				encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 0x3f];

				encodedIndex += 4;

				// If we are chunking, let's put a chunk separator down.
				if (isChunked)
				{
					// this assumes that CHUNK_SIZE % 4 == 0
					if (encodedIndex == nextSeparatorIndex)
					{
						Array.Copy(CHUNK_SEPARATOR, 0, encodedData, encodedIndex, CHUNK_SEPARATOR.Length);
						chunksSoFar++;
						nextSeparatorIndex = (CHUNK_SIZE * (chunksSoFar + 1)) + (chunksSoFar * CHUNK_SEPARATOR.Length);
						encodedIndex += CHUNK_SEPARATOR.Length;
					}
				}
			}

			// form integral number of 6-bit groups
			dataIndex = i * 3;

			if (fewerThan24bits == EIGHTBIT)
			{
				b1 = binaryData[dataIndex];
				k = (sbyte)(b1 & 0x03);
				sbyte val1 = ((b1 & SIGN) == 0) ? (sbyte)(b1 >> 2) : (sbyte)((b1) >> 2 ^ 0xc0);
				encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
				encodedData[encodedIndex + 1] = lookUpBase64Alphabet[k << 4];
				encodedData[encodedIndex + 2] = PAD;
				encodedData[encodedIndex + 3] = PAD;
			}
			else if (fewerThan24bits == SIXTEENBIT)
			{

				b1 = binaryData[dataIndex];
				b2 = binaryData[dataIndex + 1];
				l = (sbyte)(b2 & 0x0f);
				k = (sbyte)(b1 & 0x03);

				sbyte val1 = ((b1 & SIGN) == 0) ? (sbyte)(b1 >> 2) : (sbyte)((b1) >> 2 ^ 0xc0);
				sbyte val2 = ((b2 & SIGN) == 0) ? (sbyte)(b2 >> 4) : (sbyte)((b2) >> 4 ^ 0xf0);

				encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
				encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | (k << 4)];
				encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2];
				encodedData[encodedIndex + 3] = PAD;
			}

			if (isChunked)
			{
				// we also add a separator to the end of the final chunk.
				if (chunksSoFar < nbrChunks)
					Array.Copy(CHUNK_SEPARATOR, 0, encodedData, encodedDataLength - CHUNK_SEPARATOR.Length, CHUNK_SEPARATOR.Length);
			}

			return encodedData;
		}

		/// <summary>
		/// Decodes Base64 data into octects
		/// </summary>
		/// <param name="base64Data"> Byte array containing Base64 data </param>
		/// <returns> Array containing decoded data. </returns>
		public static sbyte[] decodeBase64(sbyte[] base64Data)
		{
			// RFC 2045 requires that we discard ALL non-Base64 characters
			base64Data = discardNonBase64(base64Data);

			// handle the edge case, so we don't have to worry about it later
			if (base64Data.Length == 0)
				return new sbyte[0];

			int numberQuadruple = base64Data.Length / FOURBYTE;
			sbyte[] decodedData = null;
			sbyte b1 = 0, b2 = 0, b3 = 0, b4 = 0, marker0 = 0, marker1 = 0;

			// Throw away anything not in base64Data

			int encodedIndex = 0;
			int dataIndex = 0;
			{
				// this sizes the output array properly - rlw
				int lastData = base64Data.Length;
				// ignore the '=' padding
				while (base64Data[lastData - 1] == PAD)
				{
					if (--lastData == 0)
						return new sbyte[0];
				}
				decodedData = new sbyte[lastData - numberQuadruple];
			}

			for (int i = 0; i < numberQuadruple; i++)
			{
				dataIndex = i * 4;
				marker0 = base64Data[dataIndex + 2];
				marker1 = base64Data[dataIndex + 3];

				b1 = base64Alphabet[base64Data[dataIndex]];
				b2 = base64Alphabet[base64Data[dataIndex + 1]];

				if (marker0 != PAD && marker1 != PAD)
				{
					//No PAD e.g 3cQl
					b3 = base64Alphabet[marker0];
					b4 = base64Alphabet[marker1];

					decodedData[encodedIndex] = (sbyte)(b1 << 2 | b2 >> 4);
					decodedData[encodedIndex + 1] = (sbyte)(((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
					decodedData[encodedIndex + 2] = (sbyte)(b3 << 6 | b4);
				}
				else if (marker0 == PAD)
					//Two PAD e.g. 3c[Pad][Pad]
					decodedData[encodedIndex] = (sbyte)(b1 << 2 | b2 >> 4);
				else if (marker1 == PAD)
				{
					//One PAD e.g. 3cQ[Pad]
					b3 = base64Alphabet[marker0];

					decodedData[encodedIndex] = (sbyte)(b1 << 2 | b2 >> 4);
					decodedData[encodedIndex + 1] = (sbyte)(((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
				}
				encodedIndex += 3;
			}
			return decodedData;
		}

		/// <summary>
		/// Discards any characters outside of the base64 alphabet, per the requirements on page 25 of RFC 2045 - "Any
		/// characters outside of the base64 alphabet are to be ignored in base64 encoded data."
		/// </summary>
		/// <param name="data"> The base-64 encoded data to groom </param>
		/// <returns> The data, less non-base64 characters (see RFC 2045). </returns>
		internal static sbyte[] discardNonBase64(sbyte[] data)
		{
			sbyte[] groomedData = new sbyte[data.Length];
			int bytesCopied = 0;

			for (int i = 0; i < data.Length; i++)
			{
				if (isBase64(data[i]))
					groomedData[bytesCopied++] = data[i];
			}

			sbyte[] packedData = new sbyte[bytesCopied];

			Array.Copy(groomedData, 0, packedData, 0, bytesCopied);

			return packedData;
		}
	}

}