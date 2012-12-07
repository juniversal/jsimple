package jsimple.oauth.extractors;

import jsimple.oauth.model.Token;
import jsimple.oauth.utils.OAuthEncoder;
import jsimple.util.CharIterator;

/**
 * Default implementation of {@AccessTokenExtractor}. Conforms to OAuth 2.0
 */
public class TokenExtractor20Impl implements AccessTokenExtractor {
    private static final String EMPTY_SECRET = "";

    /**
     * {@inheritDoc}
     */
    public Token extract(String response) {
        // Original code regex:
        // private static final String TOKEN_REGEX = "access_token=([^&]+)";

        CharIterator tokenIterator = new CharIterator(response);
        tokenIterator.skipAheadPast("access_token=");
        StringBuilder tokenBuffer = new StringBuilder();
        while (!tokenIterator.isWhitespace() && tokenIterator.curr() != '&' && !tokenIterator.atEnd())
            tokenBuffer.append(tokenIterator.currAndAdvance());
        if (tokenBuffer.toString().isEmpty())
            throw new RuntimeException("oauth_token is empty string");

        String token = OAuthEncoder.decode(tokenBuffer.toString());
        return new Token(token, EMPTY_SECRET, response);
    }
}
