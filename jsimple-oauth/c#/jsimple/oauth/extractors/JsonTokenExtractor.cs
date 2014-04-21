using System;
using System.Diagnostics;
using System.Text;

namespace jsimple.oauth.extractors {

    using OAuthException = jsimple.oauth.exceptions.OAuthException;
    using Token = jsimple.oauth.model.Token;
    using CharIterator = jsimple.util.CharIterator;

    public class JsonTokenExtractor : AccessTokenExtractor {
        public virtual Token extract(string response) {
            Debug.Assert(response != null && response.Length > 0, "Cannot extract a token from a null or empty String");

            try {
                // Original regex
                // private Pattern accessTokenPattern = Pattern.compile("\"access_token\":\\s*\"(\\S*?)\"");

                CharIterator tokenIterator = new CharIterator(response);

                tokenIterator.skipAheadPast("\"access_token\":");
                tokenIterator.advancePastWhitespace();
                tokenIterator.checkAndAdvance('\"');

                StringBuilder tokenBuffer = new StringBuilder();
                while (!tokenIterator.Whitespace && tokenIterator.curr() != '"' && !tokenIterator.atEnd())
                    tokenBuffer.Append(tokenIterator.currAndAdvance());

                tokenIterator.checkAndAdvance('\"');

                return new Token(tokenBuffer.ToString(), "", response);
            }
            catch (Exception e) {
                throw new OAuthException("Cannot extract an access token. Response was: " + response, e);
            }
        }
    }

}