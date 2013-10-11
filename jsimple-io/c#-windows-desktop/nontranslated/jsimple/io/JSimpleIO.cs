using jsimple.net;

namespace jsimple.io
{
    public class JSimpleIO
    {
        private static bool initialized = false;


        /// <summary>
        /// This method should be called before jsimple.net is used.  It's normally called at app startup. It initializes
        /// any factory classes to use the default implementation appropriate for the current platform.
        /// </summary>
        public static void init()
        {
            HttpRequest.Factory = new WindowsDesktopHttpRequest.WindowsDesktopHttpRequestFactory();
            SocketListener.Factory = new DotNetTcpIpSocketListener.DotNetSocketListenerFactory();
            Paths.Instance = new WindowsDesktopPaths();

            initialized = true;
        }
    }
}