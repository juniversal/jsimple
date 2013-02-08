using System;

namespace jsimple.oauth.exceptions
{

	/// <summary>
	/// @author: Pablo Fernandez
	/// </summary>
	public class OAuthConnectionException : OAuthException
	{
		private const string MSG = "There was a problem while creating a connection to the remote service.";

		public OAuthConnectionException(Exception e) : base(MSG, e)
		{
		}
	}

}