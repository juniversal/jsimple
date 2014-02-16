namespace jsimple.oauth.utils
{

	using UrlDecoder = jsimple.net.UrlDecoder;
	using UrlEncoder = jsimple.net.UrlEncoder;

	/// <summary>
	/// @author: Pablo Fernandez
	/// </summary>
	public class OAuthEncoder
	{
		public static string encode(string plain)
		{
			string encoded = UrlEncoder.encode(plain);
			encoded = encoded.Replace("*", "%2A");
			encoded = encoded.Replace("+", "%20");
			encoded = encoded.Replace("%7E", "~");
			return encoded;
		}

		public static string decode(string encoded)
		{
			return UrlDecoder.decode(encoded);
		}
	}

}