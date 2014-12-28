/*
 * Copyright (c) 2012-2014, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

using System;
using System.IO;
using System.Net;
using System.Threading;
using jsimple.io;

namespace jsimple.net
{
    public class WindowsDesktopHttpRequest : HttpRequest
    {
        private readonly HttpWebRequest httpWebRequest;
        private int timeout = 30000;            // Default the timeout to 30 seconds

        public WindowsDesktopHttpRequest(String url)
        {
            httpWebRequest = (HttpWebRequest)WebRequest.Create(url);
        }

        public override void setMethod(string value) {
            httpWebRequest.Method = value;
        }

        public override void setTimeout(int value)
        {
            timeout = value;
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
            httpWebRequest.Timeout = timeout;
            return new DotNetStreamOutputStream(httpWebRequest.GetRequestStream());
        }

        public override HttpResponse send()
        {
            // If timeout already set (say in createRequestBodyStream) then this is actually ignored
            httpWebRequest.Timeout = timeout;
            return new WindowsDesktopHttpResponse((HttpWebResponse)httpWebRequest.GetResponse());
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

        public class WindowsDesktopHttpRequestFactory : HttpRequestFactory
        {
            public HttpRequest createHttpRequest(String url)
            {
                return new WindowsDesktopHttpRequest(url);
            }
        };

    }
}