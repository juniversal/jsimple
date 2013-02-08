namespace jsimple.oauth.services
{

	/// <summary>
	/// Unix epoch timestamp generator.
	/// <p/>
	/// This class is useful for stubbing in tests.
	/// 
	/// @author Pablo Fernandez
	/// </summary>
	public interface TimestampService
	{
		/// <summary>
		/// Returns the unix epoch timestamp in seconds
		/// </summary>
		/// <returns> timestamp </returns>
		string TimestampInSeconds {get;}

		/// <summary>
		/// Returns a nonce (unique value for each request)
		/// </summary>
		/// <returns> nonce </returns>
		string Nonce {get;}
	}

}