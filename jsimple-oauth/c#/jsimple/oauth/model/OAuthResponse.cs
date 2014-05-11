namespace jsimple.oauth.model {

    using IOUtils = jsimple.io.IOUtils;
    using InputStream = jsimple.io.InputStream;
    using HttpResponse = jsimple.net.HttpResponse;


    /// <summary>
    /// Represents an HTTP Response to an OAuth request.  Note that this method was named Response in the original Scribe;
    /// for JSimple it's called OAuthResponse so that API client use the two parallel named classes of OAuthRequest and
    /// OAuthResponse.
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public class OAuthResponse {
        private const string EMPTY = "";

        private int code;
        private string body = null;
        private HttpResponse httpResponse;

        internal OAuthResponse(HttpResponse httpResponse) {
            this.httpResponse = httpResponse;
            code = httpResponse.StatusCode;
        }

        /// <summary>
        /// Obtains the HTTP Response body
        /// </summary>
        /// <returns> response body </returns>
        public virtual string Body {
            get {
                if (body == null)
                    body = IOUtils.toStringFromUtf8Stream(BodyStream);
                return body;
            }
        }

        /// <summary>
        /// Obtains the meaningful stream of the HttpUrlConnection, either inputStream or errorInputStream, depending on the
        /// status code
        /// </summary>
        /// <returns> input stream / error stream </returns>
        public virtual InputStream BodyStream {
            get {
                return httpResponse.BodyStream;
            }
        }

        public virtual HttpResponse HttpResponse {
            get {
                return httpResponse;
            }
        }

        /// <summary>
        /// Obtains the HTTP status code
        /// </summary>
        /// <returns> the status code </returns>
        public virtual int Code {
            get {
                return code;
            }
        }

        /// <summary>
        /// Obtains a single HTTP Header value, or null if not present.
        /// </summary>
        /// <param name="name"> the header name </param>
        /// <returns> header value or null </returns>
        public virtual string getHeader(string name) {
            return httpResponse.getHeader(name);
        }
    }
}