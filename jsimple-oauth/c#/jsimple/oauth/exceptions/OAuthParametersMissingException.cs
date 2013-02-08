namespace jsimple.oauth.exceptions
{

	using OAuthRequest = jsimple.oauth.model.OAuthRequest;

	/// <summary>
	/// Specialized exception that represents a missing OAuth parameter.
	/// 
	/// @author Pablo Fernandez
	/// </summary>
	public class OAuthParametersMissingException : OAuthException
	{
		private const long serialVersionUID = 1745308760111976671L;

		/// <summary>
		/// Default constructor.
		/// </summary>
		/// <param name="request"> OAuthRequest that caused the error </param>
		public OAuthParametersMissingException(OAuthRequest request) : base("Could not find oauth parameters in request: " + request + ". OAuth parameters must be specified with the addOAuthParameter() method")
		{
		}
	}

}