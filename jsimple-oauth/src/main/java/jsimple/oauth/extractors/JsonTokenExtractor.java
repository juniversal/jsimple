package jsimple.oauth.extractors;

import jsimple.oauth.exceptions.OAuthException;
import jsimple.oauth.model.Token;
import jsimple.util.CharIterator;

public class JsonTokenExtractor implements AccessTokenExtractor {
    public Token extract(String response) {
        assert response != null && !response.isEmpty() : "Cannot extract a token from a null or empty String";

        try {
            // Original regex
            // private Pattern accessTokenPattern = Pattern.compile("\"access_token\":\\s*\"(\\S*?)\"");

            CharIterator tokenIterator = new CharIterator(response);

            tokenIterator.skipAheadPast("\"access_token\":");
            tokenIterator.advancePastWhitespace();
            tokenIterator.checkAndAdvance('\"');

            StringBuilder tokenBuffer = new StringBuilder();
            while (!tokenIterator.isWhitespace() && tokenIterator.curr() != '"' && !tokenIterator.atEnd())
                tokenBuffer.append(tokenIterator.currAndAdvance());

            tokenIterator.checkAndAdvance('\"');

            return new Token(tokenBuffer.toString(), "", response);
        } catch (RuntimeException e) {
            throw new OAuthException("Cannot extract an access token. Response was: " + response, e);
        }
    }
}
