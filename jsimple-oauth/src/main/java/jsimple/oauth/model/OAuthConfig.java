package jsimple.oauth.model;

import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 * Parameter object that groups OAuth config values
 * 
 * @author Pablo Fernandez
 */
public class OAuthConfig
{
  private final String apiKey;
  private final String apiSecret;
  private final @Nullable String callback;
  private final @Nullable SignatureType signatureType;
  private final @Nullable String scope;
  private final @Nullable OutputStream debugStream;
  
  public OAuthConfig(String key, String secret)
  {
    this(key, secret, null, null, null, null);
  }

  public OAuthConfig(String key, String secret, @Nullable String callback, @Nullable SignatureType type,
                     @Nullable String scope, @Nullable OutputStream stream)
  {
    this.apiKey = key;
    this.apiSecret = secret;
    this.callback = callback;
    this.signatureType = type;
    this.scope = scope;
    this.debugStream = stream;
  }

  public String getApiKey()
  {
    return apiKey;
  }

  public String getApiSecret()
  {
    return apiSecret;
  }

  public @Nullable String getCallback()
  {
    return callback;
  }

  public @Nullable SignatureType getSignatureType()
  {
    return signatureType;
  }

  public @Nullable String getScope()
  {
    return scope;
  }

  public boolean hasScope()
  {
    return scope != null;
  }

  public void log(String message)
  {
    if (debugStream != null)
    {
      message = message + "\n";
      try
      {
        debugStream.write(message.getBytes("UTF8"));
      }
      catch (Exception e)
      {
        throw new RuntimeException("there were problems while writting to the debug stream", e);
      }
    }
  }
}
