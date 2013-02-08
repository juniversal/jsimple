namespace jsimple.oauth.builder.api
{

	using Token = jsimple.oauth.model.Token;
	using Verb = jsimple.oauth.model.Verb;

	public class GoogleOAuthApi : DefaultOAuthApi10a
	{
		public override string AccessTokenEndpoint
		{
			get
			{
				return "https://www.google.com/accounts/OAuthGetAccessToken";
			}
		}

		public override string RequestTokenEndpoint
		{
			get
			{
				return "https://www.google.com/accounts/OAuthGetRequestToken";
			}
		}

		public override string AccessTokenVerb
		{
			get
			{
				return Verb.GET;
			}
		}

		public override string RequestTokenVerb
		{
			get
			{
				return Verb.GET;
			}
		}

		public override string getAuthorizationUrl(Token requestToken)
		{
			return "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=" + requestToken.TokenString;
		}
	}

}