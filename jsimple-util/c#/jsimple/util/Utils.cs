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
	}

}