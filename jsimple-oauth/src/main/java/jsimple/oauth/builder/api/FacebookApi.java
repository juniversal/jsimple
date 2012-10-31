package jsimple.oauth.builder.api;

import jsimple.oauth.model.OAuthConfig;
import jsimple.oauth.utils.OAuthEncoder;

public class FacebookApi extends DefaultApi20 {
    private static final String AUTHORIZE_URL = "https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://graph.facebook.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        // Old code:
        // Preconditions.checkValidUrl(config.getCallback(),
        //      "Must provide a valid url as callback. Facebook does not support OOB");
        assert config.getCallback() != null && !config.getCallback().isEmpty() :
                "Must provide a valid url as callback. Facebook does not support OOB";

        String url = "https://www.facebook.com/dialog/oauth?client_id=" + config.getApiKey() + "&redirect_uri=" +
                OAuthEncoder.encode(config.getCallback());

        // Append scope if present
        String scope = config.getScope();
        if (scope != null)
            url += "&scope=" + OAuthEncoder.encode(scope);

        return url;
    }
}
