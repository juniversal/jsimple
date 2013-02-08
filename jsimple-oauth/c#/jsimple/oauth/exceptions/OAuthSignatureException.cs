using System;

namespace jsimple.oauth.exceptions
{

	/// <summary>
	/// Specialized exception that represents a problem in the signature
	/// 
	/// @author Pablo Fernandez
	/// </summary>
	public class OAuthSignatureException : OAuthException
	{
		private const long serialVersionUID = 1L;

		/// <summary>
		/// Default constructor
		/// </summary>
		/// <param name="stringToSign"> plain string that gets signed (HMAC-SHA, etc) </param>
		/// <param name="e">            original exception </param>
		public OAuthSignatureException(string stringToSign, Exception e) : base("Error while signing string: " + stringToSign, e)
		{
		}
	}

}