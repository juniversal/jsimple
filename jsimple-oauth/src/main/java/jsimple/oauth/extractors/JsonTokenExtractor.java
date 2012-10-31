package jsimple.oauth.extractors;

import jsimple.oauth.exceptions.OAuthException;
import jsimple.oauth.model.Token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonTokenExtractor implements AccessTokenExtractor {
    private Pattern accessTokenPattern = Pattern.compile("\"access_token\":\\s*\"(\\S*?)\"");

    @Override
    public Token extract(String response) {
        assert response != null && !response.isEmpty() : "Cannot extract a token from a null or empty String";

        Matcher matcher = accessTokenPattern.matcher(response);
        if (matcher.find()) {
            String group = matcher.group(1);
            assert group != null : "nullness";
            return new Token(group, "", response);
        } else {
            throw new OAuthException("Cannot extract an access token. Response was: " + response);
        }
    }

}