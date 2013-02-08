namespace jsimple.oauth.builder.api
{

	using Token = jsimple.oauth.model.Token;

	public class DropBoxOAuthApi : DefaultOAuthApi10a
	{
		public override string AccessTokenEndpoint
		{
			get
			{
				return "https://api.dropbox.com/1/oauth/access_token";
			}
		}

		public override string getAuthorizationUrl(Token requestToken)
		{
			return "https://www.dropbox.com/1/oauth/authorize?oauth_token=" + requestToken.TokenString;
		}

		public override string RequestTokenEndpoint
		{
			get
			{
				return "https://api.dropbox.com/1/oauth/request_token";
			}
		}

	}
}