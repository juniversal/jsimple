using System;
using System.Text;

namespace jsimple.oauth.extractors {

    using Token = jsimple.oauth.model.Token;
    using OAuthEncoder = jsimple.oauth.utils.OAuthEncoder;
    using CharIterator = jsimple.util.CharIterator;

    /// <summary>
    /// Default implementation of {@RequestTokenExtractor} and {@AccessTokenExtractor}. Conforms to OAuth 1.0a
    /// <p/>
    /// The process for extracting access and request tokens is similar so this class can do both things.
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public class TokenExtractorImpl : RequestTokenExtractor, AccessTokenExtractor {
        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public virtual Token extract(string response) {
            // Original code regexes
            // private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
            // private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");

            CharIterator tokenIterator = new CharIterator(response);
            tokenIterator.skipAheadPast("oauth_token=");
            StringBuilder tokenBuffer = new StringBuilder();
            while (!tokenIterator.Whitespace && tokenIterator.curr() != '&' && !tokenIterator.atEnd())
                tokenBuffer.Append(tokenIterator.read());
            if (tokenBuffer.ToString().Length == 0)
                throw new Exception("oauth_token is empty string");

            CharIterator secretIterator = new CharIterator(response);
            secretIterator.skipAheadPast("oauth_token_secret=");
            StringBuilder secretBuffer = new StringBuilder();
            while (!secretIterator.Whitespace && secretIterator.curr() != '&' && !secretIterator.atEnd())
                tokenBuffer.Append(secretIterator.read());

            string token = OAuthEncoder.decode(tokenBuffer.ToString());
            string secret = OAuthEncoder.decode(secretBuffer.ToString());
            return new Token(token, secret, response);
        }
    }

}