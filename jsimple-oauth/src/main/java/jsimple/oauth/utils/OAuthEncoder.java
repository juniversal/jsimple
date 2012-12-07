package jsimple.oauth.utils;

import jsimple.net.UrlDecoder;
import jsimple.net.UrlEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Pablo Fernandez
 */
public class OAuthEncoder {
    public static String encode(String plain) {
        String encoded = UrlEncoder.encode(plain);
        encoded = encoded.replace("*", "%2A");
        encoded = encoded.replace("+", "%20");
        encoded = encoded.replace("%7E", "~");
        return encoded;
    }

    public static String decode(String encoded) {
        return UrlDecoder.decode(encoded);
    }
}
