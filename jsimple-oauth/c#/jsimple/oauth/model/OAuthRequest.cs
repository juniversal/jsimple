using System;
using System.Diagnostics;
using System.Collections.Generic;

namespace jsimple.oauth.model {

    using File = jsimple.io.File;
    using IOUtils = jsimple.io.IOUtils;
    using InputStream = jsimple.io.InputStream;
    using OutputStream = jsimple.io.OutputStream;
    using Logger = jsimple.logging.Logger;
    using LoggerFactory = jsimple.logging.LoggerFactory;
    using jsimple.net;
    using OAuthConnectionException = jsimple.oauth.exceptions.OAuthConnectionException;
    using OAuthException = jsimple.oauth.exceptions.OAuthException;
    using ByteArrayRange = jsimple.util.ByteArrayRange;
    using PlatformUtils = jsimple.util.PlatformUtils;
    using TextualPath = jsimple.util.TextualPath;



    /// <summary>
    /// The representation of an OAuth HttpRequest.
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public class OAuthRequest {
        internal static readonly Logger logger = LoggerFactory.getLogger("OAuthRequest");

        private string url;
        private string verb;
        private ParameterList queryStringParams;
        private ParameterList bodyParams;
        private IDictionary<string, string> headers;
        private ByteArrayRange bytePayload = null;
        private string stringPayload = null;
        private File filePayload = null;
        private HttpRequest httpRequest = null;
        private int? timeout = null;
        private IDictionary<string, string> oauthParameters;

        public const string DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

        private const string OAUTH_PREFIX = "oauth_";

        /// <summary>
        /// Construct an OAuthRequest to apply to specified verb against the specified resource URL.
        /// </summary>
        /// <param name="verb"> HTTP verb/method </param>
        /// <param name="url">  resource URL </param>
        public OAuthRequest(string verb, string url) {
            this.verb = verb;
            this.url = url;
            this.queryStringParams = new ParameterList();
            this.bodyParams = new ParameterList();
            this.headers = new Dictionary<string, string>();

            this.oauthParameters = new Dictionary<string, string>();
        }

        /// <summary>
        /// Construct an OAuthRequest to apply to specified verb against the specified resource.
        /// </summary>
        /// <param name="verb">         HTTP verb/method </param>
        /// <param name="baseUrl">      base URL, including server/port and optionally start of path; this should NOT end with a /
        ///                     character </param>
        /// <param name="resourcePath"> path to resource on server, which will be appended to baseUrl </param>
        public OAuthRequest(string verb, string baseUrl, TextualPath resourcePath) : this(verb, baseUrl + UrlEncoder.encode(resourcePath)) {
        }

        /// <summary>
        /// Adds an OAuth parameter.
        /// </summary>
        /// <param name="key">   name of the parameter </param>
        /// <param name="value"> value of the parameter </param>
        /// <exception cref="IllegalArgumentException"> if the parameter is not an OAuth parameter </exception>
        public virtual void addOAuthParameter(string key, string value) {
            oauthParameters[checkKey(key)] = value;
        }

        private string checkKey(string key) {
            if (key.StartsWith(OAUTH_PREFIX) || key.Equals(OAuthConstants.SCOPE))
                return key;
            else
                throw new System.ArgumentException(string.Format("OAuth parameters must either be '{0}' or start with '{1}'", OAuthConstants.SCOPE, OAUTH_PREFIX));
        }

        /// <summary>
        /// Returns the <seealso cref="Map"/> containing the key-value pair of parameters.
        /// </summary>
        /// <returns> parameters as map </returns>
        public virtual IDictionary<string, string> OauthParameters {
            get {
                return oauthParameters;
            }
        }

        public override string ToString() {
            return string.Format("@OAuthRequest({0}, {1})", Verb, Url);
        }

        /// <summary>
        /// Execute the request and return a <seealso cref="jsimple.oauth.model.OAuthResponse"/>
        /// </summary>
        /// <returns> Http Response </returns>
        /// <exception cref="RuntimeException"> if the connection cannot be created. </exception>
        public virtual OAuthResponse send() {
            try {
                createConnection();
                return doSend();
            }
            catch (Exception e) {
                throw new OAuthConnectionException(e);
            }
        }

        private void createConnection() {
            string completeUrl = CompleteUrl;
            if (httpRequest == null)
                httpRequest = HttpRequest.create(completeUrl);
        }

        /// <summary>
        /// Returns the complete url (host + resource + encoded querystring parameters).
        /// </summary>
        /// <returns> the complete url. </returns>
        public virtual string CompleteUrl {
            get {
                return queryStringParams.appendTo(url);
            }
        }

        internal virtual OAuthResponse doSend() {
            HttpRequest httpReq = httpRequest;
            Debug.Assert(httpReq != null, "nullness");

            httpReq.Method = this.verb;

            if (timeout != null)
                httpReq.Timeout = (int)timeout;

            foreach (string key in headers.Keys)
                httpReq.setHeader(key, headers.GetValueOrNull(key));

            if (verb.Equals("PUT") || verb.Equals("POST"))
                addBody(httpReq);

            try {
                OAuthResponse response;

                if (logger.TraceEnabled) {
                    long startTime = PlatformUtils.CurrentTimeMillis;

                    response = new OAuthResponse(httpReq.send());

                    long duration = PlatformUtils.CurrentTimeMillis - startTime;
                    logger.trace("{} {}; took {}ms", verb, Url, duration);
                }
                else
                    response = new OAuthResponse(httpReq.send());

                return response;
            }
            catch (UnknownHostException e) {
                throw new OAuthException("The IP address of a host could not be determined.", e);
            }
        }

        private void addBody(HttpRequest httpReq) {
            // Set default content type if none is set
            if (httpReq.getHeader(HttpRequest.HEADER_CONTENT_TYPE) == null)
                httpReq.setHeader(HttpRequest.HEADER_CONTENT_TYPE, DEFAULT_CONTENT_TYPE);

            if (filePayload != null) {
                httpReq.setHeader(HttpRequest.HEADER_CONTENT_LENGTH, Convert.ToString(filePayload.Size));

                using (InputStream fileStream = filePayload.openForRead()) {
                    using (OutputStream bodyStream = httpReq.createRequestBodyStream()) {
                        fileStream.copyTo(bodyStream);
                    }
                }
            }
            else {
                ByteArrayRange byteArrayRange = ByteBodyContents;
                httpReq.setHeader(HttpRequest.HEADER_CONTENT_LENGTH, Convert.ToString(byteArrayRange.Length));
                using (OutputStream bodyStream = httpReq.createRequestBodyStream()) {
                    bodyStream.write(byteArrayRange);
                }
            }
        }

        /// <summary>
        /// Add an HTTP Header to the Request
        /// </summary>
        /// <param name="key">   the header name </param>
        /// <param name="value"> the header value </param>
        public virtual void addHeader(string key, string value) {
            this.headers[key] = value;
        }

        /// <summary>
        /// Add a body Parameter (for POST/ PUT Requests)
        /// </summary>
        /// <param name="key">   the parameter name </param>
        /// <param name="value"> the parameter value </param>
        public virtual void addBodyParameter(string key, string value) {
            this.bodyParams.add(key, value);
        }

        /// <summary>
        /// Add a query string parameter
        /// </summary>
        /// <param name="key">   the parameter name </param>
        /// <param name="value"> the parameter value </param>
        public virtual void addQueryStringParameter(string key, string value) {
            this.queryStringParams.add(key, value);
        }

        /// <summary>
        /// Add the passed parameters to the query string
        /// </summary>
        /// <param name="params"> parameters </param>
        public virtual void addQueryStringParameters(HttpRequestParams @params) {
            foreach (string name in @params.Names)
                addQueryStringParameter(name, @params.getValue(name));
        }

        /// <summary>
        /// Add body payload.
        /// <p/>
        /// This method is used when the HTTP body is not a form-url-encoded string, but another thing. Like for example
        /// XML.
        /// <p/>
        /// Note: The contents are not part of the OAuth signature
        /// </summary>
        /// <param name="payload"> the body of the request </param>
        public virtual void addPayload(string payload) {
            this.stringPayload = payload;
        }

        /// <summary>
        /// Overloaded version for byte arrays
        /// </summary>
        /// <param name="payload"> </param>
        public virtual void addPayload(sbyte[] payload) {
            addPayload(new ByteArrayRange(payload));
        }

        /// <summary>
        /// Overloaded version for byte arrays
        /// </summary>
        /// <param name="payload"> </param>
        public virtual void addPayload(ByteArrayRange payload) {
            this.bytePayload = payload;
        }

        /// <summary>
        /// Overloaded version for byte arrays
        /// </summary>
        /// <param name="filePayload"> </param>
        public virtual void addPayload(File filePayload) {
            this.filePayload = filePayload;
        }

        /// <summary>
        /// Get a <seealso cref="jsimple.oauth.model.ParameterList"/> with the query string parameters.
        /// </summary>
        /// <returns> a <seealso cref="jsimple.oauth.model.ParameterList"/> containing the query string parameters. </returns>
        /// <exception cref="jsimple.oauth.exceptions.OAuthException"> if the request URL is not valid. </exception>
        public virtual ParameterList QueryStringParams {
            get {
                ParameterList result = new ParameterList();
                string queryString = (new Url(url)).Query;
                result.addQueryString(queryString);
                result.addAll(queryStringParams);
                return result;
            }
        }

        /// <summary>
        /// Obtains a <seealso cref="jsimple.oauth.model.ParameterList"/> of the body parameters.
        /// </summary>
        /// <returns> a <seealso cref="jsimple.oauth.model.ParameterList"/>containing the body parameters. </returns>
        public virtual ParameterList BodyParams {
            get {
                return bodyParams;
            }
        }

        /// <summary>
        /// Obtains the URL of the HTTP request.
        /// </summary>
        /// <returns> the original URL of the HTTP request </returns>
        public virtual string Url {
            get {
                return url;
            }
        }

        /// <summary>
        /// Returns the URL without the port and the query string part.
        /// </summary>
        /// <returns> the OAuth-sanitized URL </returns>
        public virtual string SanitizedUrl {
            get {
                // Old code: return url.replaceAll("\\?.*", "").replace("\\:\\d{4}", "");
    
                string sanitizedUrl = url;
    
                int queryStringStart = url.IndexOf('?');
                if (queryStringStart != -1)
                    sanitizedUrl = sanitizedUrl.Substring(0, queryStringStart);
                int colonStart = url.IndexOf(':');
                if (colonStart != -1)
                    sanitizedUrl = sanitizedUrl.Substring(0, colonStart);
    
                return sanitizedUrl;
            }
        }

        internal virtual ByteArrayRange ByteBodyContents {
            get {
                if (bytePayload != null)
                    return bytePayload;
    
                string body = (stringPayload != null) ? stringPayload : bodyParams.asFormUrlEncodedString();
                return IOUtils.toUtf8BytesFromString(body);
            }
        }

        /// <summary>
        /// Returns the HTTP Verb
        /// </summary>
        /// <returns> the verb </returns>
        public virtual string Verb {
            get {
                return verb;
            }
        }

        /// <summary>
        /// Returns the connection headers as a <seealso cref="java.util.Map"/>
        /// </summary>
        /// <returns> map of headers </returns>
        public virtual IDictionary<string, string> Headers {
            get {
                return headers;
            }
        }

        /// <summary>
        /// Sets the timeout for the underlying <seealso cref="jsimple.net.HttpRequest"/>
        /// </summary>
        /// <param name="timeoutInMillis"> duration of the timeout </param>
        public virtual int Timeout {
            set {
                this.timeout = value;
            }
        }

        /// <summary>
        /// We need this in order to stub the connection object for test cases
        /// </summary>
        /// <param name="httpRequest"> </param>
        internal virtual HttpRequest HttpRequest {
            set {
                this.httpRequest = value;
            }
        }
    }

}