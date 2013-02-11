namespace jsimple.oauth.oauth
{

	using OAuthRequest = jsimple.oauth.model.OAuthRequest;
	using Token = jsimple.oauth.model.Token;
	using Verifier = jsimple.oauth.model.Verifier;


	/// <summary>
	/// The main Scribe object.
	/// <p/>
	/// A facade responsible for the retrieval of request and access tokens and for the signing of HTTP requests.
	/// 
	/// @author Pablo Fernandez
	/// </summary>
	public interface OAuthService
	{
		/// <summary>
		/// Retrieve the request token.
		/// </summary>
		/// <returns> request token </returns>
		Token RequestToken {get;}

		/// <summary>
		/// Retrieve the access token
		/// </summary>
		/// <param name="requestToken"> request token (obtained previously; not required for OAuth 2) </param>
		/// <param name="verifier">     verifier code </param>
		/// <returns> access token </returns>
		Token getAccessToken(Token requestToken, Verifier verifier);

		/// <summary>
		/// Signs am OAuth request
		/// </summary>
		/// <param name="accessToken"> access token (obtained previously) </param>
		/// <param name="request">     request to sign </param>
		void signRequest(Token accessToken, OAuthRequest request);

		/// <summary>
		/// Returns the OAuth version of the service.
		/// </summary>
		/// <returns> oauth version as string </returns>
		string Version {get;}

		/// <summary>
		/// Returns the URL where you should redirect your users to authenticate your application.
		/// </summary>
		/// <param name="requestToken"> the request token you need to authorize; not used for OAuth2 </param>
		/// <returns> the URL where you should redirect your users </returns>
		string getAuthorizationUrl(Token requestToken);
	}

}