using System;
using System.IO;
using System.Net;
using System.Threading;
using jsimple.io;

namespace jsimple.net
{
    public class WindowsPhoneHttpRequest : HttpRequest
    {
        private readonly HttpWebRequest httpWebRequest;
        private int timeout = 30000;            // Default the timeout to 30 seconds

        public WindowsPhoneHttpRequest(String url)
        {
            httpWebRequest = (HttpWebRequest)WebRequest.Create(url);
        }

        public override string Method
        {
            set { httpWebRequest.Method = value; }
        }

        public override int Timeout
        {
            set { timeout = value; }
        }

        public override void setHeader(string name, string value)
        {
            // For certain headers, they need to be set via properties rather than the Headers collection.  We check
            // for the most common of these (but not all of them) below.
            if (name.Equals(HEADER_ACCEPT))
                httpWebRequest.Accept = value;
            else if (name.Equals(HEADER_CONTENT_TYPE))
                httpWebRequest.ContentType = value;
            else if (name.Equals(HEADER_CONTENT_LENGTH))
                httpWebRequest.ContentLength = long.Parse(value);
            else httpWebRequest.Headers[name] = value;
        }

        public override string getHeader(string name)
        {
            if (name.Equals(HEADER_ACCEPT))
                return httpWebRequest.Accept;
            else if (name.Equals(HEADER_CONTENT_TYPE))
                return httpWebRequest.ContentType;
            else return httpWebRequest.Headers[name];
        }

        public override OutputStream createRequestBodyStream()
        {
            var dataReady = new AutoResetEvent(false);
            Stream stream = null;
            var callback = new AsyncCallback(delegate(IAsyncResult asynchronousResult)
            {
                stream = httpWebRequest.EndGetRequestStream(asynchronousResult);
                dataReady.Set();
            });

            httpWebRequest.BeginGetRequestStream(callback, httpWebRequest);
            if (!dataReady.WaitOne(timeout))
                throw new SocketTimeoutException("Could not get request stream for " + httpWebRequest.RequestUri +
                                                 " request in " + timeout + "ms");

            return new DotNetStreamOutputStream(stream);
        }

        public override HttpResponse send()
        {
            var dataReady = new AutoResetEvent(false);
            HttpWebResponse response = null;
            var callback = new AsyncCallback(delegate(IAsyncResult asynchronousResult)
            {
                response = (HttpWebResponse)httpWebRequest.EndGetResponse(asynchronousResult);
                dataReady.Set();
            });

            httpWebRequest.BeginGetResponse(callback, httpWebRequest);

            if (!dataReady.WaitOne(timeout))
                throw new SocketTimeoutException("Could not get response for " + httpWebRequest.RequestUri +
                                                 " request in " + timeout + "ms");

            return new WindowsPhoneHttpResponse(response);
        }

#if false


    @Override public void setTimeout(int timeoutInMillis) {
        httpUrlConnection.setConnectTimeout(timeoutInMillis);
        httpUrlConnection.setReadTimeout(timeoutInMillis);
    }

    @Override public void setHeader(String name, String value) {
        httpUrlConnection.setRequestProperty(name, value);
    }

    @Override public String getHeader(String name) {
        return httpUrlConnection.getRequestProperty(name);
    }

    @Override public OutputStream getRequestBodyStream() {
        if (bodyStream == null) {
            try {
                httpUrlConnection.setDoOutput(true);
                bodyStream = new JSimpleOutputStreamOnJavaStream(httpUrlConnection.getOutputStream());
            } catch (java.io.IOException e) {
                throw JavaIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        return bodyStream;
    }

    @Override public HttpResponse getResponse() {
        // TODO: The doc seems to say that, for example, for a 404 error the connect call will throw a FileNotFoundException, and the caller can use getErrorStream to read the bod.  Test that & change to catch such exceptions here, so caller gets a valid response object
        // Scribe called getErrorStream when response code not in: return getCode() >= 200 && getCode() < 400;

        try {
            httpUrlConnection.connect();
            return new HttpResponse(httpUrlConnection);
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromDotNetIOException(e);
        }
    }

 protected override void backgroundWorker_DoWork(object sender, DoWorkEventArgs e) 
        { 
            ManualResetEvent signal = new ManualResetEvent(false);  

           

            int startTime = (int)(DateTime.Now.Ticks / TIME_UNIT); 
            DoWorkInternal(signal); 

            signal.WaitOne(maxRunTime); 
            int duration = (int)(DateTime.Now.Ticks / TIME_UNIT) - startTime; 

            if (duration < minRunTime) 
                Thread.Sleep(minRunTime - duration); 
        }
#endif

        public class WindowsPhoneHttpRequestFactory : HttpRequestFactory
        {
            public HttpRequest createHttpRequest(String url)
            {
                return new WindowsPhoneHttpRequest(url);
            }
        };

    }
}