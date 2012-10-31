using jsimple.io;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace jsimple.net
{
    class HttpRequest : HttpRequestBase
    {
        private HttpWebRequest httpWebRequest;
        private OutputStream bodyStream;
        private int timeout;

        public HttpRequest(String url)
        {
            httpWebRequest = (HttpWebRequest)WebRequest.Create(url);
        }

        public string Method
        {
            set
            {
                httpWebRequest.Method = value;
            }
        }

        public int Timeout
        {
            set
            {
                timeout = value;
            }
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
                throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
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
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
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
    }
}
