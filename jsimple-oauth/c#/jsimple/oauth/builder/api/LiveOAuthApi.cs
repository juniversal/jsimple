using System.Diagnostics;

namespace jsimple.oauth.builder.api
{

	using AccessTokenExtractor = jsimple.oauth.extractors.AccessTokenExtractor;
	using JsonTokenExtractor = jsimple.oauth.extractors.JsonTokenExtractor;
	using OAuthConfig = jsimple.oauth.model.OAuthConfig;
	using OAuthEncoder = jsimple.oauth.utils.OAuthEncoder;

	public class LiveOAuthApi : DefaultOAuthApi20
	{
		public override string AccessTokenEndpoint
		{
			get
			{
				return "https://oauth.live.com/token?grant_type=authorization_code";
			}
		}

		public override string getAuthorizationUrl(OAuthConfig config)
		{
			// Old code:
			// Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Live does not support OOB");
			Debug.Assert(config.Callback != null && config.Callback.Length > 0, "Must provide a valid url as callback. Live does not support OOB");

			string url = "https://oauth.live.com/authorize?client_id=" + config.ApiKey + "&redirect_uri=" + OAuthEncoder.encode(config.Callback) + "&response_type=code";
			// Append scope if present
			string scope = config.Scope;
			if (scope != null)
				url += OAuthEncoder.encode(scope);

			return url;
		}

		public override AccessTokenExtractor AccessTokenExtractor
		{
			get
			{
				return new JsonTokenExtractor();
			}
		}
	}
}