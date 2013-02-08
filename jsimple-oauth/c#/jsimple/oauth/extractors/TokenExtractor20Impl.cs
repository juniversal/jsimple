using System;
using System.Text;

namespace jsimple.oauth.extractors
{

	using Token = jsimple.oauth.model.Token;
	using OAuthEncoder = jsimple.oauth.utils.OAuthEncoder;
	using CharIterator = jsimple.util.CharIterator;

	/// <summary>
	/// Default implementation of {@AccessTokenExtractor}. Conforms to OAuth 2.0
	/// </summary>
	public class TokenExtractor20Impl : AccessTokenExtractor
	{
		private const string EMPTY_SECRET = "";

		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual Token extract(string response)
		{
			// Original code regex:
			// private static final String TOKEN_REGEX = "access_token=([^&]+)";

			CharIterator tokenIterator = new CharIterator(response);
			tokenIterator.skipAheadPast("access_token=");
			StringBuilder tokenBuffer = new StringBuilder();
			while (!tokenIterator.Whitespace && tokenIterator.curr() != '&' && !tokenIterator.atEnd())
				tokenBuffer.Append(tokenIterator.currAndAdvance());
			if (tokenBuffer.ToString().Length == 0)
				throw new Exception("oauth_token is empty string");

			string token = OAuthEncoder.decode(tokenBuffer.ToString());
			return new Token(token, EMPTY_SECRET, response);
		}
	}

}