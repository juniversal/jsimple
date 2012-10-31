package jsimple.oauth.extractors;

import jsimple.oauth.exceptions.OAuthParametersMissingException;
import jsimple.oauth.model.OAuthRequest;
import jsimple.oauth.utils.OAuthEncoder;

import java.util.Map;

/**
 * Default implementation of {@link HeaderExtractor}. Conforms to OAuth 1.0a
 *
 * @author Pablo Fernandez
 */
public class HeaderExtractorImpl implements HeaderExtractor {
    private static final String PARAM_SEPARATOR = ", ";
    private static final String PREAMBLE = "OAuth ";

    /**
     * {@inheritDoc}
     */
    public String extract(OAuthRequest request) {
        checkPreconditions(request);
        Map<String, String> parameters = request.getOauthParameters();
        StringBuffer header = new StringBuffer(parameters.size() * 20);
        header.append(PREAMBLE);
        for (String key : parameters.keySet()) {
            if (header.length() > PREAMBLE.length()) {
                header.append(PARAM_SEPARATOR);
            }
            header.append(String.format("%s=\"%s\"", key, OAuthEncoder.encode(parameters.get(key))));
        }
        return header.toString();
    }

    private void checkPreconditions(OAuthRequest request) {
        assert request != null : "Cannot extract a header from a null object";

        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0) {
            throw new OAuthParametersMissingException(request);
        }
    }

}
