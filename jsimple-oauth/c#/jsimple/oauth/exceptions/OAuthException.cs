using System;

namespace jsimple.oauth.exceptions
{



	/// <summary>
	/// Default scribe exception. Represents a problem in the OAuth signing process
	/// 
	/// @author Pablo Fernandez
	/// </summary>
	public class OAuthException : Exception
	{

		/// <summary>
		/// Default constructor
		/// </summary>
		/// <param name="message"> message explaining what went wrong </param>
		/// <param name="e">       original exception </param>
		public OAuthException(string message, Exception e) : base(message, e)
		{
		}

		/// <summary>
		/// No-exception constructor. Used when there is no original exception
		/// </summary>
		/// <param name="message"> message explaining what went wrong </param>
		public OAuthException(string message) : base(message, null)
		{
		}

		private const long serialVersionUID = 1L;
	}

}