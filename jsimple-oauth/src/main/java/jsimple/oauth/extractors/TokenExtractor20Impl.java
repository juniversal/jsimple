package jsimple.oauth.extractors;

import jsimple.oauth.exceptions.OAuthException;
import jsimple.oauth.model.Token;
import jsimple.oauth.utils.OAuthEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of {@AccessTokenExtractor}. Conforms to OAuth 2.0
 */
public class TokenExtractor20Impl implements AccessTokenExtractor {
    private static final String TOKEN_REGEX = "access_token=([^&]+)";
    private static final String EMPTY_SECRET = "";

    /**
     * {@inheritDoc}
     */
    public Token extract(String response) {
        assert response != null && !response.isEmpty() :
                "Response body is incorrect. Can't extract a token from an empty string";

        Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
        if (matcher.find()) {
            String group = matcher.group(1);
            assert group != null : "nullness";
            String token = OAuthEncoder.decode(group);
            return new Token(token, EMPTY_SECRET, response);
        } else {
            throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
        }
    }
}
