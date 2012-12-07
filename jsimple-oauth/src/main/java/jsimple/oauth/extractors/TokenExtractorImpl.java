package jsimple.oauth.extractors;

import jsimple.oauth.model.Token;
import jsimple.oauth.utils.OAuthEncoder;
import jsimple.util.CharIterator;

/**
 * Default implementation of {@RequestTokenExtractor} and {@AccessTokenExtractor}. Conforms to OAuth 1.0a
 * <p/>
 * The process for extracting access and request tokens is similar so this class can do both things.
 *
 * @author Pablo Fernandez
 */
public class TokenExtractorImpl implements RequestTokenExtractor, AccessTokenExtractor {
    /**
     * {@inheritDoc}
     */
    public Token extract(String response) {
        // Original code regexes
        // private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
        // private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");

        CharIterator tokenIterator = new CharIterator(response);
        tokenIterator.skipAheadPast("oauth_token=");
        StringBuilder tokenBuffer = new StringBuilder();
        while (!tokenIterator.isWhitespace() && tokenIterator.curr() != '&' && !tokenIterator.atEnd())
            tokenBuffer.append(tokenIterator.currAndAdvance());
        if (tokenBuffer.toString().isEmpty())
            throw new RuntimeException("oauth_token is empty string");

        CharIterator secretIterator = new CharIterator(response);
        secretIterator.skipAheadPast("oauth_token_secret=");
        StringBuilder secretBuffer = new StringBuilder();
        while (!secretIterator.isWhitespace() && secretIterator.curr() != '&' && !secretIterator.atEnd())
            tokenBuffer.append(secretIterator.currAndAdvance());

        String token = OAuthEncoder.decode(tokenBuffer.toString());
        String secret = OAuthEncoder.decode(secretBuffer.toString());
        return new Token(token, secret, response);
    }
}
