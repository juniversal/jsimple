namespace jsimple.oauth.builder.api
{

	using AccessTokenExtractor = jsimple.oauth.extractors.AccessTokenExtractor;
	using TokenExtractor20Impl = jsimple.oauth.extractors.TokenExtractor20Impl;
	using OAuthConfig = jsimple.oauth.model.OAuthConfig;
	using Verb = jsimple.oauth.model.Verb;
	using OAuth20ServiceImpl = jsimple.oauth.oauth.OAuth20ServiceImpl;
	using OAuthService = jsimple.oauth.oauth.OAuthService;

	/// <summary>
	/// Default implementation of the OAuth protocol, version 2.0 (draft 11)
	/// <p/>
	/// This class is meant to be extended by concrete implementations of the API, providing the endpoints and
	/// endpoint-http-verbs.
	/// <p/>
	/// If your Api adheres to the 2.0 (draft 11) protocol correctly, you just need to extend this class and define the
	/// getters for your endpoints.
	/// <p/>
	/// If your Api does something a bit different, you can override the different extractors or services, in order to
	/// fine-tune the process. Please read the javadocs of the interfaces to get an idea of what to do.
	/// 
	/// @author Diego Silveira
	/// </summary>
	public abstract class DefaultOAuthApi20 : OAuthApi
	{

		/// <summary>
		/// Returns the access token extractor.
		/// </summary>
		/// <returns> access token extractor </returns>
		public virtual AccessTokenExtractor AccessTokenExtractor
		{
			get
			{
				return new TokenExtractor20Impl();
			}
		}

		/// <summary>
		/// Returns the verb for the access token endpoint (defaults to GET)
		/// </summary>
		/// <returns> access token endpoint verb </returns>
		public virtual string AccessTokenVerb
		{
			get
			{
				return Verb.GET;
			}
		}

		/// <summary>
		/// Returns the URL that receives the access token requests.
		/// </summary>
		/// <returns> access token URL </returns>
		public abstract string AccessTokenEndpoint {get;}

		/// <summary>
		/// Returns the URL where you should redirect your users to authenticate your application.
		/// </summary>
		/// <param name="config"> OAuth 2.0 configuration param object </param>
		/// <returns> the URL where you should redirect your users </returns>
		public abstract string getAuthorizationUrl(OAuthConfig config);

		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual OAuthService createService(OAuthConfig config)
		{
			return new OAuth20ServiceImpl(this, config);
		}

	}

}