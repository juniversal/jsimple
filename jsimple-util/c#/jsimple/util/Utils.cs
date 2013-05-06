using System;

namespace jsimple.util
{


	/// <summary>
	/// Created with IntelliJ IDEA.
	/// 
	/// @author Bret Johnson
	/// @since 1/4/13 2:15 AM
	/// </summary>
	public class Utils
	{
		/// <summary>
		/// Return an array of bytes containing as elements this method's parameters.  Parameters are ints but they should be
		/// in a legal range for a byte (technically -128 to 127, but treating the range like 0-255 also works as expected).
		/// <p/>
		/// This method exists mostly to avoid casting problems populating a byte[] literals inline--casting issues caused by
		/// integer literals always being ints not bytes in Java (and thus requiring a byte cast--compile issue #1) and
		/// translated C# always treating hex literals as unsigned and not allowing ones > 127 to be cast to (signed) bytes
		/// (compile issue #2). Anyway, that's all avoided just by passing in ints here.
		/// </summary>
		/// <param name="bytes"> 0 or more bytes to convert into an array of bytes </param>
		/// <returns> array of bytes </returns>
		public static sbyte[] byteArrayFromBytes(params int[] bytes)
		{
			int length = bytes.Length;

			sbyte[] byteArray = new sbyte[length];
			for (int i = 0; i < length; i++)
				byteArray[i] = (sbyte) bytes[i];

			return byteArray;
		}


//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public static long parseRFC3339Long(String datestring) throws java.text.ParseException, IndexOutOfBoundsException
		public static long parseRFC3339Long(string datestring)
		{
			DateTime d = DateTime.Now;

			//if there is no time zone, we don't need to do any special parsing.
			if (datestring.EndsWith("Z"))
			{
				try
				{
					SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); //spec for RFC3339
					d = s.parse(datestring);
				} //try again with optional decimals
				catch (java.text.ParseException pe)
				{
					SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"); //spec for RFC3339 (with fractional seconds)
					s.Lenient = true;
					d = s.parse(datestring);
				}
				return d.Time;
			}

			//step one, split off the timezone.
			string firstpart = datestring.Substring(0, datestring.LastIndexOf('-'));
			string secondpart = datestring.Substring(datestring.LastIndexOf('-'));

			//step two, remove the colon from the timezone offset
			secondpart = secondpart.Substring(0, secondpart.IndexOf(':')) + secondpart.Substring(secondpart.IndexOf(':') + 1);
			datestring = firstpart + secondpart;
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); //spec for RFC3339
			try
			{
				d = s.parse(datestring);
			} //try again with optional decimals
			catch (java.text.ParseException pe)
			{
				s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"); //spec for RFC3339 (with fractional seconds)
				s.Lenient = true;
				d = s.parse(datestring);
			}
			return d.Time;
		}
	}

}