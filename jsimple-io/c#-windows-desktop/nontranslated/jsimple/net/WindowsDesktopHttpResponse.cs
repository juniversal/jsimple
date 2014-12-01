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

        public override void close()
        {
            httpWebResponse.Close();
        }

        // TODO: Test Content-Encoding, Content-Length, Content-Type, Last-Modified, and Server
        // TODO: Test non-present header to ensure returns null
        public override string getHeader(string headerName)
        {
            return httpWebResponse.Headers[headerName];
        }
    }
}