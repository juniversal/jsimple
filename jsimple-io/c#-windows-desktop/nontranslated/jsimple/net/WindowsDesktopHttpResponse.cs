using System;
using System.Net;
using jsimple.io;

namespace jsimple.net
{
    public class WindowsDesktopHttpResponse : HttpResponse
    {
        private readonly HttpWebResponse httpWebResponse;

        public WindowsDesktopHttpResponse(HttpWebResponse httpWebResponse)
        {
            this.httpWebResponse = httpWebResponse;
        }

        // TODO: Catch web exception too and either set StatusCode here or change JSimple code to throw exceptions instead
        public override int StatusCode
        {
            get { return (int)httpWebResponse.StatusCode; }
        }

        public override string StatusMessage
        {
            get { return httpWebResponse.StatusDescription; }
        }

        public override InputStream BodyStream
        {
            get { return new DotNetStreamInputStream(httpWebResponse.GetResponseStream()); }
        }

        // TODO: Test Content-Encoding, Content-Length, Content-Type, Last-Modified, and Server
        // TODO: Test non-present header to ensure returns null
        public override string getHeader(string headerName)
        {
            return httpWebResponse.Headers[headerName];
        }
    }
}