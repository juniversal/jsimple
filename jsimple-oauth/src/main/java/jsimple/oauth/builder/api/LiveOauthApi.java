package jsimple.oauth.builder.api;

import jsimple.oauth.extractors.AccessTokenExtractor;
import jsimple.oauth.extractors.JsonTokenExtractor;
import jsimple.oauth.model.OAuthConfig;
import jsimple.oauth.utils.OAuthEncoder;

public class LiveOAuthApi extends DefaultOAuthApi20 {
    @Override public String getAccessTokenEndpoint() {
        return "https://oauth.live.com/token?grant_type=authorization_code";
    }

    @Override public String getAuthorizationUrl(OAuthConfig config) {
        // Old code:
        // Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Live does not support OOB");
        assert config.getCallback() != null && !config.getCallback().isEmpty() :
                "Must provide a valid url as callback. Live does not support OOB";

        String url = "https://oauth.live.com/authorize?client_id=" + config.getApiKey() + "&redirect_uri=" +
                OAuthEncoder.encode(config.getCallback()) + "&response_type=code";
        // Append scope if present
        String scope = config.getScope();
        if (scope != null)
            url += OAuthEncoder.encode(scope);

        return url;
    }

    @Override public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}