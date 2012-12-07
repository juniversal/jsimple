package jsimple.oauth.extractors;

import jsimple.oauth.exceptions.OAuthParametersMissingException;
import jsimple.oauth.model.OAuthRequest;
import jsimple.oauth.model.ParameterList;
import jsimple.oauth.utils.OAuthEncoder;

/**
 * Default implementation of {@link BaseStringExtractor}. Conforms to OAuth 1.0a
 *
 * @author Pablo Fernandez
 */
public class BaseStringExtractorImpl implements BaseStringExtractor {
    /**
     * {@inheritDoc}
     */
    public String extract(OAuthRequest request) {
        checkPreconditions(request);
        String verb = OAuthEncoder.encode(request.getVerb());
        String url = OAuthEncoder.encode(request.getSanitizedUrl());
        String params = getSortedAndEncodedParams(request);
        return verb + "&" + url + "&" + params;
    }

    private String getSortedAndEncodedParams(OAuthRequest request) {
        ParameterList params = new ParameterList();
        params.addAll(request.getQueryStringParams());
        params.addAll(request.getBodyParams());
        params.addAll(new ParameterList(request.getOauthParameters()));
        return params.sort().asOauthBaseString();
    }

    private void checkPreconditions(OAuthRequest request) {
        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0)
            throw new OAuthParametersMissingException(request);
    }
}
