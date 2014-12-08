import jsimple.io.IOUtils;
import jsimple.net.HttpRequest;

/**
 * @author Dorin Suletea
 * @since 12/5/2014
 */
public class AzureAuthenticator {
    //receives an facebook acesstoken (obtained with fb oauth) , and generates a azure-mobile token that can be
    //used in order to query the azure-mobile db
    //pass only the acess token , do not include the expiration date
    //from the printed json coppy the authenticationToken value ,
    // this token can be passed to main.AzureMobileReader.queryAzureWithFbToken(String) in order to read the DB
    public static void obtainAzureToken(String facebookAcessToken) {
        HttpRequest req = jsimple.net.HttpRequest.create(Constants.FB_AUTH_URL);

        req.setMethod(HttpRequest.METHOD_POST);
        req.setHeader(HttpRequest.HEADER_CONTENT_LENGTH, "238");
        req.setContentTypeAndAcceptHeaders("application/json", "application/json");

        jsimple.io.Utf8OutputStreamWriter out = new jsimple.io.Utf8OutputStreamWriter(req.createRequestBodyStream());
        String reqStr = "{\"access_token\":\"" + facebookAcessToken + "\"}";
        out.write(reqStr);
        out.flush();
        out.close();

        jsimple.net.HttpResponse resp = req.send();
        String azureAuthData = IOUtils.toStringFromUtf8Stream(resp.getBodyStream());

        System.out.println(azureAuthData);
    }
}
