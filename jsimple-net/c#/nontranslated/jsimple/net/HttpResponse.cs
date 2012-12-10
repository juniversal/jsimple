using jsimple.io;

namespace jsimple.net
{
    public class HttpResponse : HttpResponseBase
    {
        public override int StatusCode
        {
            get { throw new System.NotImplementedException(); }
        }

        public override string StatusMessage
        {
            get { throw new System.NotImplementedException(); }
        }

        public override InputStream BodyStream
        {
            get { throw new System.NotImplementedException(); }
        }

        public override string getHeader(string headerName)
        {
            throw new System.NotImplementedException();
        }
    }
}