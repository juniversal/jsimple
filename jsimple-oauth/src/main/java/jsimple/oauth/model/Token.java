package jsimple.oauth.model;

import org.jetbrains.annotations.Nullable;

/**
 * Represents an OAuth token (either request or access token) and its secret
 *
 * @author Pablo Fernandez
 */
public class Token /* implements Serializable */ {
    //private static final long serialVersionUID = 715000866082812683L;

    private final String token;
    private final String secret;
    private final @Nullable String rawResponse;

    /**
     * Default constructor
     *
     * @param token  token value
     * @param secret token secret
     */
    public Token(String token, String secret) {
        this(token, secret, null);
    }

    public Token(String token, String secret, @Nullable String rawResponse) {
        this.token = token;
        this.secret = secret;
        this.rawResponse = rawResponse;
    }

    public String getTokenString() {
        return token;
    }

    public String getSecret() {
        return secret;
    }

    public String getRawResponse() {
        if (rawResponse == null)
            throw new RuntimeException("This token object was not constructed by scribe and does not have a rawResponse");
        return rawResponse;
    }

    @Override
    public String toString() {
        //return String.format("Token[%s , %s]", token, secret);
        return "Token[" + token + " , " + secret + "]";
    }
}
